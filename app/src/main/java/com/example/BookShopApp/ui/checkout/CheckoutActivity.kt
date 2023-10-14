package com.example.BookShopApp.ui.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.BookShopApp.R
import com.example.BookShopApp.data.api.apizalopay.CreateOrder
import com.example.BookShopApp.databinding.ActivityCheckoutBinding
import com.example.BookShopApp.databinding.FragmentCheckOutBinding
import com.example.BookShopApp.ui.adapter.BookListAdapter
import com.example.BookShopApp.ui.main.cart.CartFragment
import com.example.BookShopApp.ui.main.cart.CartViewModel
import com.example.BookShopApp.ui.profile.ProfileViewModel
import com.example.BookShopApp.ui.profile.ProfileViewModelFactory
import com.example.BookShopApp.ui.profile.receiver.receiver.ReceiversFragment
import com.example.BookShopApp.ui.profile.receiver.receiverinfo.ReceiverInfoFragment
import com.example.BookShopApp.ui.profile.receiver.receiverinfo.ReceiverInfoViewModel
import com.example.BookShopApp.utils.AlertMessageViewer
import com.example.BookShopApp.utils.LoadingProgressBar
import com.example.BookShopApp.utils.format.FormatMoney
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class CheckoutActivity : AppCompatActivity() {
    private var binding: ActivityCheckoutBinding? = null
    private lateinit var viewModelCheckOut: CheckOutViewModel
    private lateinit var viewModelCart: CartViewModel
    private lateinit var viewModelProfile: ProfileViewModel
    private lateinit var viewModelReceiver: ReceiverInfoViewModel
    private lateinit var loadingProgressBar: LoadingProgressBar
    private lateinit var adapter: BookListAdapter
    private var formatMoney = FormatMoney()
    private var cartId = ""
    private var check = false
    private var shippingId = 1
    private var shippingPrice = 50000.00
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        viewModelCheckOut = ViewModelProvider(this)[CheckOutViewModel::class.java]
        viewModelCart = ViewModelProvider(this)[CartViewModel::class.java]
        viewModelReceiver = ViewModelProvider(this)[ReceiverInfoViewModel::class.java]
        viewModelProfile = ViewModelProvider(
            this,
            ProfileViewModelFactory(application)
        )[ProfileViewModel::class.java]
        loadingProgressBar = LoadingProgressBar(this)
        loadingProgressBar.show()
        adapter = BookListAdapter()
        initViewModel()
        viewModelCart.getCartId()
        viewModelCart.getAllCartItem()
        viewModelProfile.getCustomer()
        viewModelReceiver.getReceiverDefault()

//        Handler().postDelayed({
//            if (binding?.idReceiverInfo?.text.toString().isEmpty()) {
//                AlertDialog.Builder(applicationContext)
//                    .setMessage("Không có địa chỉ nhận hàng, vui lòng thêm địa chỉ nhận hàng")
//                    .setCancelable(false)
//                    .setPositiveButton("Thoát") { dialog, _ ->
//                        parentFragmentManager.popBackStack()
//                        dialog.cancel()
//                    }
//                    .setNegativeButton("Đồng ý") { dialog, _ ->
//                        parentFragmentManager.beginTransaction()
//                            .replace(R.id.container, ReceiverInfoFragment())
//                            .addToBackStack("CheckOut")
//                            .commit()
//                        dialog.cancel()
//                    }
//                    .show()
//            }
//        }, 350)

        binding?.apply {
            textChangeInfor.setOnClickListener {
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.container, ReceiversFragment())
//                    .addToBackStack("CheckOut")
//                    .commit()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ReceiversFragment())
                    .addToBackStack("CheckOut")
                    .commit()
            }
            textPayment.setOnClickListener {
//                val receiverId = idReceiverInfo.text.toString().toInt()
//                viewModelCheckOut.createOrder(
//                    cartId,
//                    shippingId,
//                    receiverId
//                )
//                createOrderApi()
                payment()
                check = true
//                loadingProgressBar.show()
            }
            imageLeft.setOnClickListener {
//                parentFragmentManager.popBackStack()
                navCartFragment()
            }
            recyclerCartItem.layoutManager = LinearLayoutManager(applicationContext)
            recyclerCartItem.adapter = adapter
        }
    }

    private fun payment() {
        var token = ""
        val orderApi = CreateOrder()

        try {
            val amount = binding?.textTotalPrice?.text?.replace(Regex("\\D"), "")
            val data = orderApi.createOrder(amount.toString())
            val code = data?.getString("return_code")
            Toast.makeText(this, "return_code: $code", Toast.LENGTH_LONG).show()

            if (code == "1") {
                token = data.getString("zp_trans_token")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ZaloPaySDK.getInstance()
            .payOrder(this, token, "demozpdk://app", object : PayOrderListener {
                override fun onPaymentSucceeded(
                    transactionId: String,
                    transToken: String,
                    appTransID: String,
                ) {
                    runOnUiThread {
//                        AlertDialog.Builder(applicationContext)
//                            .setTitle("Thanh toán thành công!")
//                            .setMessage("TransactionId: $transactionId - TransToken: $transToken")
//                            .setPositiveButton("OK") { _, _ -> }
//                            .setNegativeButton("Cancel", null).show()
                        AlertMessageViewer.showAlertZalopay(
                            this@CheckoutActivity,
                            "Thanh toán thành công!",
                            "TransactionId: $transactionId - TransToken: $transToken"
                        )
                    }
                }

                override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
//                    AlertDialog.Builder(this@CheckoutActivity)
//                        .setTitle("Khách hàng hủy thanh toán bằng ZaloPay")
//                        .setMessage("zpTransToken: $zpTransToken")
//                        .setCancelable(false)
//                        .setPositiveButton("Close") { dialog, _ ->
//                            dialog.cancel()
//                        }
//                        .show()
                    AlertMessageViewer.showAlertZalopay(
                        this@CheckoutActivity,
                        "Khách hàng hủy thanh toán bằng ZaloPay",
                        "zpTransToken: $zpTransToken"
                    )
                }

                override fun onPaymentError(
                    zaloPayError: ZaloPayError,
                    zpTransToken: String,
                    appTransID: String,
                ) {
//                    AlertDialog.Builder(applicationContext)
//                        .setTitle("Thanh toán thất bại!")
//                        .setMessage("ZaloPayErrorCode: ${zaloPayError.toString()}\nTransToken: $zpTransToken")
//                        .setPositiveButton("OK") { _, _ -> }
//                        .setNegativeButton("Cancel", null).show()
                    AlertMessageViewer.showAlertZalopay(
                        this@CheckoutActivity,
                        "Thanh toán thất bại!",
                        "ZaloPayErrorCode: ${zaloPayError.toString()}\nTransToken: $zpTransToken"
                    )
                }
            })

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

    private fun initViewModel() {
        viewModelCheckOut.message.observe(this) {
            loadingProgressBar.cancel()
            if (check) {
                AlertDialog.Builder(applicationContext)
                    .setMessage(it.message.message.capitalize())
                    .setCancelable(false)
                    .setPositiveButton("Close") { dialog, _ ->
                        if (it.isState) {
//                            parentFragmentManager.popBackStack()
                            navCartFragment()
                            dialog.cancel()
                        } else {
                            dialog.cancel()
                        }
                    }
                    .show()
                check = false
            }
        }
        viewModelCart.cartItem.observe(this) { cartItem ->
            loadingProgressBar.cancel()
            binding?.apply {
                adapter.setDataCart(cartItem)
                textTotalPriceProduct.text =
                    formatMoney.formatMoney(adapter.getTotalPrice().toLong())
                textPromotionPrice.text =
                    formatMoney.formatMoney(adapter.getTotalPromotionPrice().toLong())
                getTotalPrice()
            }
        }
        viewModelCart.cartId.observe(this) {
            cartId = it
        }
        viewModelReceiver.receiverInfo.observe(this) { receiverInfo ->
            loadingProgressBar.cancel()
            if (receiverInfo != null) {
                binding?.apply {
                    idReceiverInfo.text = receiverInfo.receiverId.toString()
                    textCustomerName.text = receiverInfo.receiverName
                    textCutomerPhone.text = receiverInfo.receiverPhone
                    textCutomerAddress.text = receiverInfo.receiverAddress
                }
            }
        }
    }

    fun getTotalPrice() {
        binding?.apply {
            textShipPrice.text =
                formatMoney.formatMoney(shippingPrice.toLong())
            var totalPrice = textTotalPriceProduct.text.replace(Regex("[^\\d]"), "").toInt() +
                    shippingPrice.toInt() - textPromotionPrice.text
                .replace(Regex("[^\\d]"), "").toInt()
            textTotalPrice.text = formatMoney.formatMoney(totalPrice.toLong())
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radiobtn_express -> {
                        shippingId = 1
                        shippingPrice = 50000.00
                    }
                    R.id.radiobtn_fast -> {
                        shippingId = 2
                        shippingPrice = 30000.00
                    }
                    R.id.radiobtn_economical -> {
                        shippingId = 3
                        shippingPrice = 10000.00
                    }
                }
                textShipPrice.text =
                    formatMoney.formatMoney(shippingPrice.toLong())
                totalPrice = textTotalPriceProduct.text.replace(Regex("[^\\d]"), "").toInt() +
                        shippingPrice.toInt() - textPromotionPrice.text
                    .replace(Regex("[^\\d]"), "").toInt()
                textTotalPrice.text = formatMoney.formatMoney(totalPrice.toLong())
            }
        }
    }

    fun navCartFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CartFragment())
            .commit()
    }
}