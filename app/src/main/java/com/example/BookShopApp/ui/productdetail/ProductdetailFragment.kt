package com.example.BookShopApp.ui.productdetail

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.BookShopApp.R
import com.example.BookShopApp.data.model.response.product.ProductInfoList
import com.example.BookShopApp.databinding.FragmentProductDetailBinding
import com.example.BookShopApp.ui.author.AuthorFragment
import com.example.BookShopApp.ui.main.wishlist.WishlistViewModel
import com.example.BookShopApp.ui.productdetail.ratingbook.RatingFragment
import com.example.BookShopApp.ui.profile.ProfileFragment
import com.example.BookShopApp.ui.publisher.PublisherFragment
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.format.FormatMoney
import com.example.BookShopApp.utils.LoadingProgressBar
import com.example.BookShopApp.utils.MySharedPreferences

class ProductdetailFragment : Fragment() {
    private var binding: FragmentProductDetailBinding? = null
    private lateinit var viewModel: ProductdetailViewModel
    private lateinit var viewModelWishList: WishlistViewModel
    private var wishlist: Int = 0
    private val formatMoney = FormatMoney()
    private var authorId = 0
    private var publisherId = 0
    private var sizeWishList = 0
    private lateinit var loadingProgressBar: LoadingProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductdetailViewModel::class.java]
        viewModelWishList = ViewModelProvider(this)[WishlistViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        loadingProgressBar = LoadingProgressBar(requireContext())
        loadingProgressBar.show()
        viewModelWishList.getWishList(10, 1, 100)
        val productId = arguments?.getString("bookId")?.toInt()
        productId?.let {
            viewModel.getProductInfo(it)
        }
        readmoreInfo()
        activity?.let { MySharedPreferences.init(it.applicationContext) }
        binding?.apply {
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            imageAccount.setOnClickListener {
                val profileFragment = ProfileFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, profileFragment)
                    .addToBackStack("productFragment")
                    .commit()
            }
            textNameAuthor.setOnClickListener {
                val authorFragment = AuthorFragment()
                val bundle = Bundle()
                bundle.putString("authorId", authorId.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, authorFragment.apply { arguments = bundle })
                    .addToBackStack("productFragment")
                    .commit()
            }
            textAdditemtocart.setOnClickListener {
                val str = textNum.text.toString().split(" ")
                val quantityRemaining = str[str.size - 1].toInt()
                if (quantityRemaining > 0) {
                    productId?.let { productId ->
                        viewModel.addItemToCart(productId)
                        Handler().postDelayed({
                            viewModel.getProductInfo(productId)
                        }, 500)
                        AlertMessageViewer.showAlertDialogMessage(
                            requireContext(),
                            "Đã thêm sản phẩm vào giỏ hàng!"
                        )
                    }
                } else {
                    AlertMessageViewer.showAlertDialogMessage(
                        requireContext(),
                        "Sản phẩm này tạm hết"
                    )
                }
            }
            imageFavorite.setOnClickListener {
                productId?.let { productId -> itemWishList(productId) }
            }
            imageArrow.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("publisherId", publisherId.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, PublisherFragment().apply { arguments = bundle })
                    .addToBackStack("ProductDetail")
                    .commit()
            }
            textRatingLevel.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("bookId", productId.toString())
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, RatingFragment().apply { arguments=bundle })
                    .addToBackStack("ProductDetail").commit()
            }
        }
    }

    private fun initViewModel() {
        viewModel.productInfo.observe(viewLifecycleOwner) { productInfoList ->
            productInfoList?.let {
                bindData(it)
                authorId = it.author.authorId
                wishlist = it.product.wishlist
                publisherId = it.supplier.supplier_id
            }
        }
        viewModelWishList.wishList.observe(viewLifecycleOwner) { wishlist ->
            sizeWishList = wishlist.wishlist.size
//            Log.d("Size", wishlist.wishlist.toString())
        }
    }

    private fun itemWishList(productId: Int) {
        MySharedPreferences.putInt("productId", productId)
        if (wishlist == 0) {
            if (sizeWishList >= 10) {
                AlertMessageViewer.showAlertDialogMessage(
                    requireContext(),
                    "Chỉ lưu được 10 sản phẩm trong wishlist"
                )
            } else {
                viewModel.addItemToWishList(productId)
                wishlist = 1
                Toast.makeText(context, "Đã thêm vào wishlist của bạn!", Toast.LENGTH_SHORT).show()
                MySharedPreferences.putInt("wishlist", 1)
                binding?.imageFavorite?.setImageResource(R.drawable.ic_favorite)
                binding?.imageFavorite?.setBackgroundResource(R.drawable.bg_ellipse_favor)
            }
        } else {
            viewModel.removeItemInWishList(productId)
            wishlist = 0
            Toast.makeText(context, "Đã xóa khỏi wishlist của bạn!", Toast.LENGTH_SHORT).show()
            MySharedPreferences.putInt("wishlist", 0)
            binding?.imageFavorite?.setImageResource(R.drawable.ic_favor_white)
            binding?.imageFavorite?.setBackgroundResource(R.drawable.bg_ellipse)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(productInfoList: ProductInfoList) {
        binding?.apply {
            Glide.with(root)
                .load(productInfoList.product.thumbnail)
                .fitCenter()
                .into(imagePro)
            textName.text = productInfoList.product.name
            rating.rating=productInfoList.product.ratingLevel.toFloat()
            textRatingLevel.text=productInfoList.product.ratingLevel.toString()
            textNum.text =
                resources.getString(R.string.quantity) + " " + (productInfoList.product.quantity - productInfoList.product.quantitySold)
            textMa.text =
                resources.getString(R.string.productId) + " " + productInfoList.product.productId
            textDescription.text = productInfoList.product.description
            textPrice.text =
                formatMoney.formatMoney(productInfoList.product.price.toDouble().toLong())
            textNameAuthor.text = " " + setAuthorName(productInfoList.author.authorName)
            textNcc.text =
                resources.getString(R.string.supplier) + " " + productInfoList.supplier.supplier_name
            readmore.text = resources.getString(R.string.readmore)
            textPublish.text = productInfoList.supplier.supplier_name
            val wishListPre = MySharedPreferences.getInt("wishlist", -1)
            val productIdPre = MySharedPreferences.getInt("productId", -1)
            if (wishListPre != -1 && productIdPre == productInfoList.product.productId) {
                if (MySharedPreferences.getInt("wishlist", -1) == 1) {
                    imageFavorite.setBackgroundResource(R.drawable.bg_ellipse_favor)
                    imageFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    binding?.imageFavorite?.setImageResource(R.drawable.ic_favor_white)
                    binding?.imageFavorite?.setBackgroundResource(R.drawable.bg_ellipse)
                }
            } else {
                if (productInfoList.product.wishlist == 1) {
                    imageFavorite.setBackgroundResource(R.drawable.bg_ellipse_favor)
                    imageFavorite.setImageResource(R.drawable.ic_favorite)
                } else {
                    binding?.imageFavorite?.setImageResource(R.drawable.ic_favor_white)
                    binding?.imageFavorite?.setBackgroundResource(R.drawable.bg_ellipse)
                }
            }
            loadingProgressBar.cancel()
        }
    }

    private fun readmoreInfo() {
        var check = true
        binding?.apply {
            val layoutParams = constraintImageProduct.layoutParams as ConstraintLayout.LayoutParams
            readmore.setOnClickListener {
                if (check) {
                    layoutParams.dimensionRatio = "3:2"
                    constraintImageProduct.layoutParams = layoutParams
                    val newMaxLines = Integer.MAX_VALUE
                    textDescription.maxLines = newMaxLines
                    check = false
                    readmore.text = "Collapse."
                } else {
                    layoutParams.dimensionRatio = "6:5"
                    constraintImageProduct.layoutParams = layoutParams
                    val newMaxLines = 3
                    textDescription.maxLines = newMaxLines
                    readmore.text = "Read more."
                    check = true
                }
            }
        }
    }

    private fun setAuthorName(name: String): SpannableString {
        val content = SpannableString(name)
        content.setSpan(UnderlineSpan(), 0, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val underlineColor = resources.getColor(R.color.colorAuth)
        content.setSpan(
            ForegroundColorSpan(underlineColor),
            0,
            name.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return content
    }
}