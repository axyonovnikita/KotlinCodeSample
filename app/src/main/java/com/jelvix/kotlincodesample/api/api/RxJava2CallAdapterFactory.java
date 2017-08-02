package com.jelvix.kotlincodesample.api.api;

import com.jelvix.kotlincodesample.BuildConfig;
import com.jelvix.kotlincodesample.api.api.exception.ApiException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class RxJava2CallAdapterFactory extends CallAdapter.Factory {

    public static RxJava2CallAdapterFactory create() {
        return new RxJava2CallAdapterFactory(null, false);
    }

    /**
     * Returns an instance which creates asynchronous observables. Applying
     * {@link Observable#subscribeOn} has no effect on stream types created by this factory.
     */
    public static RxJava2CallAdapterFactory createAsync() {
        return new RxJava2CallAdapterFactory(null, true);
    }

    /**
     * Returns an instance which creates synchronous observables that
     * {@linkplain Observable#subscribeOn(Scheduler) subscribe on} {@code scheduler} by default.
     */
    public static RxJava2CallAdapterFactory createWithScheduler(Scheduler scheduler) {
        if (scheduler == null) throw new NullPointerException("scheduler == null");
        return new RxJava2CallAdapterFactory(scheduler, false);
    }

    private final Scheduler scheduler;
    private final boolean isAsync;

    private RxJava2CallAdapterFactory(Scheduler scheduler, boolean isAsync) {
        this.scheduler = scheduler;
        this.isAsync = isAsync;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Class<?> rawType = getRawType(returnType);

        if (rawType == Completable.class) {
            // Completable is not parameterized (which is what the rest of this method deals with) so it
            // can only be created with a single configuration.
            return new RxJava2CallAdapter(Void.class, scheduler, isAsync, false, true, false, false,
                    false, true);
        }

        boolean isFlowable = rawType == Flowable.class;
        boolean isSingle = rawType == Single.class;
        boolean isMaybe = rawType == Maybe.class;
        if (rawType != Observable.class && !isFlowable && !isSingle && !isMaybe) {
            return null;
        }

        boolean isResult = false;
        boolean isBody = false;
        Type responseType;
        if (!(returnType instanceof ParameterizedType)) {
            String name = isFlowable ? "Flowable"
                    : isSingle ? "Single"
                    : isMaybe ? "Maybe" : "Observable";
            throw new IllegalStateException(name + " return type must be parameterized"
                    + " as " + name + "<Foo> or " + name + "<? extends Foo>");
        }

        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType == Response.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Response must be parameterized"
                        + " as Response<Foo> or Response<? extends Foo>");
            }
            responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
        } else if (rawObservableType == Result.class) {
            if (!(observableType instanceof ParameterizedType)) {
                throw new IllegalStateException("Result must be parameterized"
                        + " as Result<Foo> or Result<? extends Foo>");
            }
            responseType = getParameterUpperBound(0, (ParameterizedType) observableType);
            isResult = true;
        } else {
            responseType = observableType;
            isBody = true;
        }

        return new RxJava2CallAdapter(responseType, scheduler, isAsync, isResult, isBody, isFlowable,
                isSingle, isMaybe, false);
    }


    /* inner classes */

    private final class RxJava2CallAdapter<R> implements CallAdapter<R, Object> {
        private final Type responseType;
        private final Scheduler scheduler;
        private final boolean isAsync;
        private final boolean isResult;
        private final boolean isBody;
        private final boolean isFlowable;
        private final boolean isSingle;
        private final boolean isMaybe;
        private final boolean isCompletable;

        RxJava2CallAdapter(Type responseType, Scheduler scheduler, boolean isAsync, boolean isResult,
                           boolean isBody, boolean isFlowable, boolean isSingle, boolean isMaybe,
                           boolean isCompletable) {
            this.responseType = responseType;
            this.scheduler = scheduler;
            this.isAsync = isAsync;
            this.isResult = isResult;
            this.isBody = isBody;
            this.isFlowable = isFlowable;
            this.isSingle = isSingle;
            this.isMaybe = isMaybe;
            this.isCompletable = isCompletable;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public Object adapt(Call<R> call) {
            Observable<Response<R>> responseObservable = isAsync
                    ? new CallEnqueueObservable<>(call)
                    : new CallExecuteObservable<>(call);

            Observable<?> observable;
            if (isResult) {
                observable = new ResultObservable<>(responseObservable);
            } else if (isBody) {
                observable = new BodyObservable<>(responseObservable);
            } else {
                observable = responseObservable;
            }

            if (scheduler != null) {
                observable = observable.subscribeOn(scheduler);
            }

            if (isFlowable) {
                return observable.toFlowable(BackpressureStrategy.LATEST);
            }
            if (isSingle) {
                return observable.singleOrError();
            }
            if (isMaybe) {
                return observable.singleElement();
            }
            if (isCompletable) {
                return observable.ignoreElements();
            }
            return observable;
        }
    }

    private final class ResultObservable<T> extends Observable<Result<T>> {
        private final Observable<Response<T>> upstream;

        ResultObservable(Observable<Response<T>> upstream) {
            this.upstream = upstream;
        }

        @Override protected void subscribeActual(Observer<? super Result<T>> observer) {
            upstream.subscribe(new ResultObserver<T>(observer));
        }

        private class ResultObserver<R> implements Observer<Response<R>> {
            private final Observer<? super Result<R>> observer;

            ResultObserver(Observer<? super Result<R>> observer) {
                this.observer = observer;
            }

            @Override public void onSubscribe(Disposable disposable) {
                observer.onSubscribe(disposable);
            }

            @Override public void onNext(Response<R> response) {
                observer.onNext(Result.response(response));
            }

            @Override public void onError(Throwable throwable) {
                try {
                    observer.onNext(Result.<R>error(throwable));
                } catch (Throwable t) {
                    try {
                        observer.onError(t);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(t, inner));
                    }
                    return;
                }
                observer.onComplete();
            }

            @Override public void onComplete() {
                observer.onComplete();
            }
        }
    }

    private final class BodyObservable<T> extends Observable<T> {
        private final Observable<Response<T>> upstream;

        BodyObservable(Observable<Response<T>> upstream) {
            this.upstream = upstream;
        }

        @Override protected void subscribeActual(Observer<? super T> observer) {
            upstream.subscribe(new BodyObserver<T>(observer));
        }

        private class BodyObserver<R> implements Observer<Response<R>> {
            private final Observer<? super R> observer;
            private boolean terminated;

            BodyObserver(Observer<? super R> observer) {
                this.observer = observer;
            }

            @Override public void onSubscribe(Disposable disposable) {
                observer.onSubscribe(disposable);
            }

            @Override public void onNext(Response<R> response) {
                if (response.isSuccessful()) {
                    observer.onNext(response.body());
                } else {
                    terminated = true;

                    // handle api exception
                    try {
                        int code = response.code();
                        String string = response.errorBody().string();
                        observer.onError(new ApiException(code, string));
                    } catch (IOException e) {
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override public void onComplete() {
                if (!terminated) {
                    observer.onComplete();
                }
            }

            @Override public void onError(Throwable throwable) {
                if (!terminated) {
                    // handle no internet exception
                    if (throwable instanceof ConnectException || throwable instanceof SocketTimeoutException || throwable instanceof UnknownHostException) {
                        observer.onError(ApiException.generateNoInternetError());
                    } else {
                        observer.onError(throwable);
                    }
                } else {
                    // This should never happen! onNext handles and forwards errors automatically.
                    Throwable broken = new AssertionError(
                            "This should never happen! Report as a bug with the full stacktrace.");
                    //noinspection UnnecessaryInitCause Two-arg AssertionError constructor is 1.7+ only.
                    broken.initCause(throwable);
                    RxJavaPlugins.onError(broken);
                }
            }
        }
    }

    private final class CallExecuteObservable<T> extends Observable<Response<T>> {
        private final Call<T> originalCall;

        CallExecuteObservable(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override protected void subscribeActual(Observer<? super Response<T>> observer) {
            // Since Call is a one-shot type, clone it for each new observer.
            Call<T> call = originalCall.clone();
            observer.onSubscribe(new CallDisposable(call));

            boolean terminated = false;
            try {
                Response<T> response = call.execute();
                if (!call.isCanceled()) {
                    observer.onNext(response);
                }
                if (!call.isCanceled()) {
                    terminated = true;
                    observer.onComplete();
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (terminated) {
                    RxJavaPlugins.onError(t);
                } else if (!call.isCanceled()) {
                    try {
                        // handle no internet exception
                        if (t instanceof ConnectException || t instanceof SocketTimeoutException || t instanceof UnknownHostException) {
                            observer.onError(ApiException.generateNoInternetError());
                        } else {
                            observer.onError(t);
                        }
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(t, inner));
                    }
                }
            }
        }

        private final class CallDisposable implements Disposable {
            private final Call<?> call;

            CallDisposable(Call<?> call) {
                this.call = call;
            }

            @Override public void dispose() {
                call.cancel();
            }

            @Override public boolean isDisposed() {
                return call.isCanceled();
            }
        }
    }

    private final class CallEnqueueObservable<T> extends Observable<Response<T>> {
        private final Call<T> originalCall;

        CallEnqueueObservable(Call<T> originalCall) {
            this.originalCall = originalCall;
        }

        @Override protected void subscribeActual(Observer<? super Response<T>> observer) {
            // Since Call is a one-shot type, clone it for each new observer.
            Call<T> call = originalCall.clone();
            CallCallback<T> callback = new CallCallback<>(call, observer);
            observer.onSubscribe(callback);
            call.enqueue(callback);
        }

        private final class CallCallback<T> implements Disposable, Callback<T> {
            private final Call<?> call;
            private final Observer<? super Response<T>> observer;
            boolean terminated = false;

            CallCallback(Call<?> call, Observer<? super Response<T>> observer) {
                this.call = call;
                this.observer = observer;
            }

            @Override public void onResponse(Call<T> call, Response<T> response) {
                if (call.isCanceled()) return;

                try {
                    observer.onNext(response);

                    if (!call.isCanceled()) {
                        terminated = true;
                        observer.onComplete();
                    }
                } catch (Throwable t) {
                    if (terminated) {
                        RxJavaPlugins.onError(t);
                    } else if (!call.isCanceled()) {
                        try {
                            observer.onError(t);
                        } catch (Throwable inner) {
                            Exceptions.throwIfFatal(inner);
                            RxJavaPlugins.onError(new CompositeException(t, inner));
                        }
                    }
                }
            }

            @Override public void onFailure(Call<T> call, Throwable t) {
                if (call.isCanceled()) return;

                try {
                    observer.onError(t);
                } catch (Throwable inner) {
                    Exceptions.throwIfFatal(inner);
                    RxJavaPlugins.onError(new CompositeException(t, inner));
                }
            }

            @Override public void dispose() {
                call.cancel();
            }

            @Override public boolean isDisposed() {
                return call.isCanceled();
            }
        }
    }

    private static final class Result<T> {
        public static <T> Result<T> error(Throwable error) {
            if (error == null) throw new NullPointerException("error == null");
            return new Result<>(null, error);
        }

        public static <T> Result<T> response(Response<T> response) {
            if (response == null) throw new NullPointerException("response == null");
            return new Result<>(response, null);
        }

        private final Response<T> response;
        private final Throwable error;

        private Result(Response<T> response, Throwable error) {
            this.response = response;
            this.error = error;
        }

        /**
         * The response received from executing an HTTP request. Only present when {@link #isError()} is
         * false, null otherwise.
         */
        public Response<T> response() {
            return response;
        }

        /**
         * The error experienced while attempting to execute an HTTP request. Only present when {@link
         * #isError()} is true, null otherwise.
         * <p>
         * If the error is an {@link IOException} then there was a problem with the transport to the
         * remote server. Any other exception type indicates an unexpected failure and should be
         * considered fatal (configuration error, programming error, etc.).
         */
        public Throwable error() {
            return error;
        }

        /** {@code true} if the request resulted in an error. See {@link #error()} for the cause. */
        public boolean isError() {
            return error != null;
        }
    }
}

