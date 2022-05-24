package com.example.user.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class RecyclerViewLoadMoreScroll : RecyclerView.OnScrollListener {
    private var visibleThreshold = 5
    private var mOnLoadMoreListener: OnLoadMoreListener? = null
    var loaded = false
        private set
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var mLayoutManager: RecyclerView.LayoutManager
    fun setLoaded() {
        loaded = false
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener?) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    constructor(layoutManager: LinearLayoutManager) {
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.getSpanCount()
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.getSpanCount()
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy <= 0) return
        totalItemCount = mLayoutManager.itemCount
        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions: IntArray =
                (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItem = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }
        if (!loaded && totalItemCount <= lastVisibleItem + visibleThreshold) {
            mOnLoadMoreListener?.onLoadMore()
            loaded = true
        }
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}
