package com.nkr.mashpro.base

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nkr.mashpro.BR

class BaseListAdapter<T : BaseListAdapter.ListItemViewModel>(@LayoutRes val layoutId : Int) :
    RecyclerView.Adapter<BaseListAdapter.BaseItemViewHolder<T>>()  {

    var itemList: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemViewHolder<T> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutId, parent, false)

        return BaseItemViewHolder(
           binding
        )

    }


    override fun onBindViewHolder(holder: BaseItemViewHolder<T>, position: Int) {
        val itemViewModel = this.itemList!![position]
        holder.bind(itemViewModel)

        listener?.let {
            itemViewModel.listener = it
        }


    }


    override fun getItemCount(): Int {
       return itemList?.size ?: 0


    }


    fun updateAdapterList(item_list : List<T>) {
        this.itemList = item_list
    }




    class  BaseItemViewHolder<T : ListItemViewModel> (val binding : ViewDataBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemViewModel : T ){
            binding.setVariable(BR.listingViewModel, itemViewModel)
            binding.executePendingBindings()
        }

    }


    /**
     * listener class related
     */
    var listener: AdapterListener? = null

    interface AdapterListener {
      fun onItemClick(item : ListItemViewModel)
     //   fun onUploadConfirm(prod_uid : String)
     //   fun onUploadReject(prod_uid: String)
    }



    abstract class ListItemViewModel {
       abstract var listener : AdapterListener?
    }

}













