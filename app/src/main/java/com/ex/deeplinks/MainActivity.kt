package com.ex.deeplinks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var button1: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1 = findViewById(R.id.button)

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->

                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Log.e(
                        "asdfghjmgfds", "onCreate: " + (deepLink?.getQueryParameter("uriString")
                            ?: "Empty")
                    )
                }

            }
            .addOnFailureListener(this) { e ->
                Log.w(
                    "asdfghjmgfds",
                    "getDynamicLink:onFailure",
                    e
                )
            }
        // [END get_deep_link]

        button1.setOnClickListener {
            shareData()
        }


    }

    private fun shareData() {
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://www.rajattest.com/")
            domainUriPrefix = "https://robinhood99.page.link"

            socialMetaTagParameters {
                imageUrl = Uri.parse("https://avatars.githubusercontent.com/u/42009770?v=4")
                title = "Example of a Dynamic Link"
                description = "This link works whether the app is installed or not!"
            }// Open links with this app on Android
            buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
            androidParameters("com.ex.deeplinks") {
                minimumVersion = 1

            }
            iosParameters("com.example.ios") {
                appStoreId = "123456789"
                minimumVersion = "1.0.1"
            }
            googleAnalyticsParameters {
                source = "orkut"
                medium = "social"
                campaign = "example-promo"
            }
            itunesConnectAnalyticsParameters {
                providerToken = "123456"
                campaignToken = "example-promo"
            }
        }

        val dynamicLinkUri = dynamicLink.uri
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send $dynamicLinkUri");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.send_to)))
    }
}