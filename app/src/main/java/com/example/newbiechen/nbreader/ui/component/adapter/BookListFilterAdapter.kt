package com.example.newbiechen.nbreader.ui.component.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newbiechen.nbreader.R
import com.example.newbiechen.nbreader.databinding.ItemBookListFilterBinding
import com.example.newbiechen.nbreader.uilts.LogHelper
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

/**
 *  author : newbiechen
 *  date : 2019-08-05 18:31
 *  description :
 */

typealias OnTagSelectedListener = (key: String, value: Int) -> Unit

class BookListFilterAdapter : RecyclerView.Adapter<BookListFilterAdapter.BookListFilterViewHolder>() {
    companion object {
        private const val TAG = "BookListFilterAdapter"
    }

    // 存储数据的三个列表
    private val mTitleList = mutableListOf<String>()
    private val mFiltersList = mutableListOf<List<String>>()
    private val isMultipleList = mutableListOf<Boolean>()

    private val mCurSelectedTagMap = mutableMapOf<String, List<Int>>()

    private var mTagSelectedListener: OnTagSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListFilterViewHolder {
        val dataBinding = ItemBookListFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookListFilterViewHolder(dataBinding)
    }

    override fun getItemCount(): Int {
        return mTitleList.size
    }

    override fun onBindViewHolder(holder: BookListFilterViewHolder, position: Int) {
        val title = mTitleList[position]
        holder.bind(title, mFiltersList[position], isMultipleList[position], mCurSelectedTagMap[title])
    }

    /**
     * 添加 TagGroup
     */
    fun addFilterTagGroup(title: String, value: List<String>, isMultiple: Boolean) {
        mTitleList.add(title)
        mFiltersList.add(value)
        isMultipleList.add(isMultiple)
    }

    fun setOnTagChangeListener(tagSelectedListener: OnTagSelectedListener) {
        mTagSelectedListener = tagSelectedListener
    }

    // 获取 tag 的选中结果
    fun getFilterResult(): Map<String, List<Int>> {
        // 复制一份 map
        return mCurSelectedTagMap.toMap()
    }

    /**
     * 重置 tag 并设置预选中的 tag
     */
    fun reset(preSelectedMap: Map<String, List<Int>>? = null) {
        // 清空当前 tag
        mCurSelectedTagMap.clear()
        if (preSelectedMap != null) {
            // 载入 preSelcted Tag
            mCurSelectedTagMap.putAll(preSelectedMap!!)
        }
        // 刷新 adapter
        notifyDataSetChanged()
    }

    inner class BookListFilterViewHolder(private val dataBinding: ItemBookListFilterBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(title: String, filters: List<String>, isMultiple: Boolean, preSelectedList: List<Int>?) {
            dataBinding.apply {
                tvTitle.text = title
                flFilter.apply {
                    val tagAdapter = object : TagAdapter<String>(filters) {
                        override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                            val tvTag = LayoutInflater.from(parent!!.context)
                                .inflate(R.layout.item_tag, parent, false) as TextView
                            tvTag.text = t
                            return tvTag
                        }
                    }

                    // 设置预选中的 tag
                    if (preSelectedList != null) {
                        tagAdapter.setSelectedList(preSelectedList.toSet())
                    }

                    setMaxSelectCount(if (isMultiple) -1 else 1)
                    setOnSelectListener {
                        // 将选中的列表存储到 map 中
                        mCurSelectedTagMap[title] = it.toList()
                    }
                    setOnTagClickListener { view, position, parent ->
                        mTagSelectedListener?.invoke(title, position)
                        true
                    }

                    adapter = tagAdapter
                }
            }
        }
    }
}