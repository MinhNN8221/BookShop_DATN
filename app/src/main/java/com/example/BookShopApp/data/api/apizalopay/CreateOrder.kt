package com.example.BookShopApp.data.api.apizalopay

import com.example.BookShopApp.data.api.HttpProvider
import com.example.BookShopApp.data.model.AppZaloPayInfo
import com.example.BookShopApp.utils.helperzalopay.Helpers
import okhttp3.FormBody
import org.json.JSONObject
import java.util.*

class CreateOrder {
    private inner class CreateOrderData(amountTotal: String) {
        var appId: String           //Định danh cho ứng dụng đã được cấp khi đăng ký ứng dụng với ZaloPay.
        var appUser: String         //Thông tin định danh của người dùng ứng dụng thanh toán đơn hàng.
        var appTime: String         //Thời gian tạo đơn hàng
        var amount: String          //giá trị của đơn hàng
        var appTransId: String      //Mã giao dịch của đơn hàng
        var embedData: String       //
        var items: String           //Item của đơn hàng, do ứng dụng tự định nghĩa
        var bankCode: String        //Mã ngân hàng
        var description: String
        var mac: String             //Thông tin chứng thực của đơn hàng

        init {
            val dateTime = Date().time
            appId = AppZaloPayInfo.APP_ID.toString()
            appUser = "Android_Demo"
            appTime = dateTime.toString()
            amount = amountTotal
            appTransId = Helpers().getAppTransId()
            embedData = "{}"
            items = "[]"
            bankCode = "zalopayapp"
            description = "Thanh toán đơn hàng cho BOOKSHOP"
            val inputHMac = "${this.appId}|${this.appTransId}|${this.appUser}|${this.amount}|${this.appTime}|${this.embedData}|${this.items}"
            mac = Helpers().getMac(AppZaloPayInfo.MAC_KEY, inputHMac).toString()
        }
    }

    fun createOrder(amount: String): JSONObject? {
        val input = CreateOrderData(amount)

        val formBody = FormBody.Builder()
            .add("app_id", input.appId)
            .add("app_user", input.appUser)
            .add("app_time", input.appTime)
            .add("amount", input.amount)
            .add("app_trans_id", input.appTransId)
            .add("embed_data", input.embedData)
            .add("item", input.items)
            .add("bank_code", input.bankCode)
            .add("description", input.description)
            .add("mac", input.mac)
            .build()

        return HttpProvider().sendPost(AppZaloPayInfo.URL_CREATE_ORDER, formBody)
    }
}