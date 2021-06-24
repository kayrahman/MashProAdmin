package com.nkr.bazaranocustomer.util

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nkr.bazaranocustomer.util.StorageUtil.pathToReference
import com.nkr.mashpro.R
import kotlinx.android.synthetic.main.upload_fragment.*
import timber.log.Timber




@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Timber.i("prod_image_url ${pathToReference(imageUrl)} and ${imageUrl}")
        Glide.with(view.context)
            .load(pathToReference(imageUrl))
            .placeholder(R.drawable.ic_twotone_photo_24)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}



@BindingAdapter("imageBannerUrl")
fun setBannerImageUrl(imageView: ImageView, url: String?) {

    try {
        if (url!!.isNotEmpty()) {
            if (url.startsWith("http")) {
                Glide.with(imageView.context).load(url)
                    .apply {
                        RequestOptions()
                            .placeholder(R.drawable.ic_baseline_video_call_24)
                    }
                    .into(imageView)
            }else{
                Glide.with(imageView.context)
                    .load(pathToReference(url))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
            }
        }


    } catch (e: Exception) {
    }

}



@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String?) {

    try {
        if (url!!.isNotEmpty()) {
            if (url!!.startsWith("http")) {
                Glide.with(imageView.context).load(url)
                    .apply {
                        RequestOptions()
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                    }
                    .into(imageView)
            } else {
                Glide.with(imageView.context).load(StorageUtil.pathToReference(url.toString()))
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .into(imageView)
            }
        } else {
            Glide.with(imageView.context).load(R.drawable.ic_baseline_account_circle_24)
                .into(imageView)

        }


    } catch (e: Exception) {
    }

}


@BindingAdapter("firestoreImageUrl")
fun setFirestoreImageUrl(imageView: ImageView, url: String?) {
    if (url != null && !url.isNullOrEmpty()) {
        Timber.i("prod_image_url ${pathToReference(url)}")
        Glide.with(imageView.context).load(pathToReference(url)).into(imageView)
    }
}



@BindingAdapter(
    value = ["bindingAdapter", "defaultRvLayout", "showDefaultDividerLine"],
    requireAll = false
)
fun setBindingAdapter(
    view: RecyclerView,
    adapter: RecyclerView.Adapter<*>,
    defaultLayout: Boolean?,
    showDefaultDividerLine: Boolean?
) {
    if (defaultLayout != null && defaultLayout) {
        val mLayoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        view.layoutManager = mLayoutManager
        view.itemAnimator = DefaultItemAnimator()
        if (showDefaultDividerLine == true) {
            view.addItemDecoration(
                DividerItemDecoration(view.context, LinearLayout.VERTICAL)
            )
        }

        // Vertical
        // OverScrollDecoratorHelper.setUpOverScroll(view, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)

    }
    view.adapter = adapter
    view.setHasFixedSize(true)

}


@BindingAdapter("adapterGridChosenImages")
fun setAdapterGridChosenImages(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
    // val mLayoutManager = GridLayoutManager(view.context, 3)
    val mLayoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
    view.layoutManager = mLayoutManager
    //OverScrollDecoratorHelper.setUpOverScroll(view, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL)
}



@BindingAdapter("layoutFullscreen")
fun View.bindLayoutFullscreen(previousFullscreen: Boolean, fullscreen: Boolean) {
    if (previousFullscreen != fullscreen && fullscreen) {
        @Suppress("DEPRECATION")
        // The new alternative is WindowCompat.setDecorFitsSystemWindows, but we can't
        // always get access to the window from a view.
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}

@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    requireAll = false
)
fun View.applySystemWindowInsetsPadding(
    previousApplyLeft: Boolean,
    previousApplyTop: Boolean,
    previousApplyRight: Boolean,
    previousApplyBottom: Boolean,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    if (previousApplyLeft == applyLeft &&
        previousApplyTop == applyTop &&
        previousApplyRight == applyRight &&
        previousApplyBottom == applyBottom
    ) {
        return
    }

    doOnApplyWindowInsets { view, insets, padding, _ ->
        val systemBars = insets.systemWindowInsets
        val left = if (applyLeft) systemBars.left else 0
        val top = if (applyTop) systemBars.top else 0
        val right = if (applyRight) systemBars.right else 0
        val bottom = if (applyBottom) systemBars.bottom else 0

        view.setPadding(
            padding.left + left,
            padding.top + top,
            padding.right + right,
            padding.bottom + bottom
        )

        insets.consumeSystemWindowInsets()
    }
}

@BindingAdapter(
    "marginLeftSystemWindowInsets",
    "marginTopSystemWindowInsets",
    "marginRightSystemWindowInsets",
    "marginBottomSystemWindowInsets",
    requireAll = false
)
fun View.applySystemWindowInsetsMargin(
    previousApplyLeft: Boolean,
    previousApplyTop: Boolean,
    previousApplyRight: Boolean,
    previousApplyBottom: Boolean,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    if (previousApplyLeft == applyLeft &&
        previousApplyTop == applyTop &&
        previousApplyRight == applyRight &&
        previousApplyBottom == applyBottom
    ) {
        return
    }

    doOnApplyWindowInsets { view, insets, _, margin ->
        val systemBars = insets.systemWindowInsets
        val left = if (applyLeft) systemBars.left else 0
        val top = if (applyTop) systemBars.top else 0
        val right = if (applyRight) systemBars.right else 0
        val bottom = if (applyBottom) systemBars.bottom else 0

        view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = margin.left + left
            topMargin = margin.top + top
            rightMargin = margin.right + right
            bottomMargin = margin.bottom + bottom
        }

        insets.consumeSystemWindowInsets()

    }
}

fun View.doOnApplyWindowInsets(
    block: (View, WindowInsetsCompat, InitialPadding, InitialMargin) -> Unit
) {
    // Create a snapshot of the view's padding & margin states
    val initialPadding = recordInitialPaddingForView(this)
    val initialMargin = recordInitialMarginForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding & margin states
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding, initialMargin)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

private fun recordInitialMarginForView(view: View): InitialMargin {
    val lp = view.layoutParams as? ViewGroup.MarginLayoutParams
        ?: throw IllegalArgumentException("Invalid view layout params")
    return InitialMargin(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}