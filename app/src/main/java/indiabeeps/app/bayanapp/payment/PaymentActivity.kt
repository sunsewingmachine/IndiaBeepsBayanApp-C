package indiabeeps.app.bayanapp.payment

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.*
import com.android.billingclient.api.PurchasesUpdatedListener
import indiabeeps.app.bayanapp.R
import indiabeeps.app.bayanapp.Splash
import indiabeeps.app.bayanapp.payment.Config.ADDON1
import indiabeeps.app.bayanapp.payment.Config.ADDON2
import indiabeeps.app.bayanapp.payment.Config.ADDON3


class PaymentActivity : AppCompatActivity() , PurchasesUpdatedListener, PaymentProductListSkuAdapter.ItemViewHolder.OnItemSelectedListener {
    private lateinit var billingClient: BillingClient
    private val skuList = listOf(ADDON1, ADDON2, ADDON3)
    private lateinit var buttonBuyProduct:Button
    private lateinit var txtPurchased:TextView
    private lateinit var webViewContribute:WebView
    private lateinit var mPaymentProductModels: MutableList<SkuDetails>
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mPaymentProductListAdapter: PaymentProductListSkuAdapter
    private lateinit var productModel: SkuDetails

    var prefs: SharedPreferences? = null
    var editor: Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        buttonBuyProduct=findViewById(R.id.buttonBuyProduct)
        txtPurchased=findViewById(R.id.txtPurchased)
        mRecyclerView=findViewById(R.id.recyclerView)
        webViewContribute = findViewById(R.id.webViewContribute)

        mPaymentProductModels = ArrayList()
        mPaymentProductListAdapter = PaymentProductListSkuAdapter(this,mPaymentProductModels, this)
        mRecyclerView.adapter = mPaymentProductListAdapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        mRecyclerView.setItemViewCacheSize(12)
//         mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = true
        mRecyclerView.layoutManager = layoutManager
        buttonBuyProduct.setOnClickListener { initPurchaseFlow() }
        setupBillingClient()

        var pur: String;
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        if(sharedPreferences.contains(Splash.sWhatPurchased))
        {
            pur = "Purchased: " +  sharedPreferences.getString(Splash.sWhatPurchased, "").toString()
            if (pur != "Purchased: ") {
                txtPurchased.text = pur
            }
        }
        else
        {
            editor.putString(Splash.sWhatPurchased, "")
            editor.apply()
            editor.commit()
            txtPurchased.text = ""
        }

        webViewContribute.loadUrl("file:///android_asset/contribute.html");
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this)
                .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully
                    Log.d("Billing","Setup Billing Done")
                    loadAllSKUs()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.e("Billing","Failed")
            }
        })
    }

    private fun loadAllSKUs() = if (billingClient.isReady) {
        Log.e("Billing","loadAllSKUs Call")
        val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.SUBS)
                .build()
        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // Process the result.
            if (skuDetailsList != null) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    createList(skuDetailsList)

                }else{
                    Log.e("Billing","Error"+billingResult.responseCode)
                }
            }else{
                Log.e("Billing","skuDetailsList null")

            }
        }

    } else {
        Log.e("Billing","Billing Client not ready")
        println("Billing Client not ready")

    }
    private fun createList(skuDetailsList: List<SkuDetails>) {
        mPaymentProductModels.clear()
        mPaymentProductModels.addAll(skuDetailsList)
        mPaymentProductListAdapter.notifyDataSetChanged()
        productModel = mPaymentProductModels[0]
    }

    private fun initPurchaseFlow() {

        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(productModel)
                .build()
        billingClient.launchBillingFlow(this, flowParams)
    }

    override fun onPurchasesUpdated(p0: BillingResult, purchases: MutableList<Purchase>?) {
        if (p0.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                acknowledgePurchase(purchase)

            }
        } else if (p0.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            println("Billing Client  error")

        }
    }


    private fun acknowledgePurchase(purchase:Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
        billingClient.acknowledgePurchase(params) {
            Toast.makeText(this,"Payment SuccessFull  "+ purchase.packageName, Toast.LENGTH_LONG).show()

            val sharedPreferences: SharedPreferences = this.getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE)
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString(Splash.sWhatPurchased, purchase.skus[0])
            editor.apply()
            editor.commit()

        }
    }

    override fun onItemSelected(item: SkuDetails?) {
        mPaymentProductListAdapter.notifyDataSetChanged()
        if (item != null) {
            productModel = item
        }
    }
}