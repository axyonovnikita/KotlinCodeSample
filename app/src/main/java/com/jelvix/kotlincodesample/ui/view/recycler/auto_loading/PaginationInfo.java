package com.jelvix.kotlincodesample.ui.view.recycler.auto_loading;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 *
 * Offset and limit for {@link AutoLoadingRecyclerView AutoLoadedRecyclerView channel}
 */
public class PaginationInfo {

    private String maxId;
    private boolean isFirstPortionLoaded;
    private boolean allPortionsLoaded;

    public void reset() {
        maxId = null;
        isFirstPortionLoaded = false;
        allPortionsLoaded = false;
    }

    public String getMaxId() {
        return maxId;
    }

    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

    public boolean isFirstPortionLoaded() {
        return isFirstPortionLoaded;
    }

    public void setFirstPortionLoaded(boolean firstPortionLoaded) {
        isFirstPortionLoaded = firstPortionLoaded;
    }

    public boolean isAllPortionsLoaded() {
        return allPortionsLoaded;
    }

    public void setAllPortionsLoaded(boolean allPortionsLoaded) {
        this.allPortionsLoaded = allPortionsLoaded;
    }
}
