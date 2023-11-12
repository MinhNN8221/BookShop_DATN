package com.example.BookShopApp.data.api.apizalopay

import com.example.BookShopApp.data.api.HttpProvider
import com.example.BookShopApp.data.model.AppZaloPayInfo
import com.example.BookShopApp.utils.helperzalopay.Helpers
import okhttp3.FormBody
import org.json.JSONObject
import java.util.*

class CreateOrder {
    private inner class CreateOrderData(amount: String) {
        var AppId: String
        var AppUser: String
        var AppTime: String
        var Amount: String
        var AppTransId: String
        var EmbedData: String
        var Items: String
        var BankCode: String
        var Description: String
        var Mac: String

        init {
            val appTime = Date().time
            AppId = AppZaloPayInfo.APP_ID.toString()
            AppUser = "Android_Demo"
            AppTime = appTime.toString()
            Amount = amount
            AppTransId = Helpers().getAppTransId()
            EmbedData = "{}"
            Items = "[]"
            BankCode = "zalopayapp"
            Description = "Thanh toán đơn hàng cho BOOKSHOP"
            val inputHMac = "${this.AppId}|${this.AppTransId}|${this.AppUser}|${this.Amount}|${this.AppTime}|${this.EmbedData}|${this.Items}"
            Mac = Helpers().getMac(AppZaloPayInfo.MAC_KEY, inputHMac).toString()
        }
    }

    fun createOrder(amount: String): JSONObject? {
        val input = CreateOrderData(amount)

        val formBody = FormBody.Builder()
            .add("app_id", input.AppId)
            .add("app_user", input.AppUser)
            .add("app_time", input.AppTime)
            .add("amount", input.Amount)
            .add("app_trans_id", input.AppTransId)  //Mã app_trans_id của giao dịch
            .add("embed_data", input.EmbedData)
            .add("item", input.Items)
            .add("bank_code", input.BankCode)
            .add("description", input.Description)
            .add("mac", input.Mac)
            .build()

        return HttpProvider().sendPost(AppZaloPayInfo.URL_CREATE_ORDER, formBody)
    }
}