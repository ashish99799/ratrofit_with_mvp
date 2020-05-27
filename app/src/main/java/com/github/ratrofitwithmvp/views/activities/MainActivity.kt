package com.github.ratrofitwithmvp.views.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.ratrofitwithmvp.R
import com.github.ratrofitwithmvp.model.data.RowData
import com.github.ratrofitwithmvp.presenters.MainActivityPresenter
import com.github.ratrofitwithmvp.utils.LoadImage
import com.github.ratrofitwithmvp.utils.NewIntentWithData
import com.github.ratrofitwithmvp.utils.ToastMessage
import com.github.ratrofitwithmvp.views.listeners.MainActivityListener
import com.github.ratrofitwithmvp.wegates.RefreshLayoutHelper
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_cell.view.*

class MainActivity : AppCompatActivity(),
    MainActivityListener,
    OnLoadmoreListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    // Variable Declaration
    var context: Context? = null
    var query: String = ""
    var page: Int = 1
    lateinit var listProducts: ArrayList<RowData>

    var presenter: MainActivityPresenter? = null
    var githubUserAdapter: GithubUserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup SupportActionBar to over Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        context = this

        // Init View
        initView()
    }

    fun initView() {
        // Setup Presenter
        presenter =
            MainActivityPresenter(this)

        rvGithubUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        smartRefresh.isEnableRefresh = false
        smartRefresh.isEnableOverScrollBounce = true
        RefreshLayoutHelper.initToLoadMoreStyle(smartRefresh, this)

        // setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        searchView.setOnQueryTextListener(this)
        val searchClose =
            searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView
        searchClose.setOnClickListener {
            searchView.clearFocus()
            searchView.setQuery("", false)
            onRefresh()
        }
    }

    override fun onRefresh() {
        query = ""
        page = 1
        listProducts = ArrayList<RowData>()
        githubUserAdapter = GithubUserAdapter(context!!, listProducts)
        rvGithubUsers.adapter = githubUserAdapter
        hideProgress()
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        // Call Api second page
        page++
        presenter!!.getSearchUser(context!!, query, page)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (TextUtils.isEmpty(newText)) {
            return false
        }

        query = newText.toString()
        page = 1
        listProducts = ArrayList<RowData>()
        presenter!!.getSearchUser(context!!, query, page)
        return false
    }

    override fun showProgress() {
        // Api Calling started
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgress() {
        // Api Calling ended
        swipeRefreshLayout.isRefreshing = false
        if (smartRefresh.isLoading) {
            smartRefresh.finishLoadmore()
        }
    }

    override fun onSuccess(data: ArrayList<RowData>) {
        // Success API Response
        if (listProducts.isEmpty()) {
            listProducts.addAll(data)
            githubUserAdapter = GithubUserAdapter(context!!, listProducts)
            rvGithubUsers.adapter = githubUserAdapter
        } else {
            githubUserAdapter!!.AddAdapterList(data)
        }
    }

    override fun onFailure(message: String) {
        // Error & Failure
        ToastMessage(message)
    }

    inner class GithubUserAdapter(
        private var context: Context,
        private val AdapterList: ArrayList<RowData>
    ) : RecyclerView.Adapter<GithubUserAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main_cell, parent, false)
            return ViewHolder(v)
        }

        fun AddAdapterList(list: ArrayList<RowData>) {
            for (element in list) {
                if (!AdapterList.contains(element)) {
                    AdapterList.add(element)
                }
            }
//            this.AdapterList.addAll(list)
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return AdapterList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            LoadImage(AdapterList[position].avatar_url!!, holder.imgAvatar)

            holder.lblUserName.text = AdapterList[position].login

            holder.itemView.setOnClickListener {
                val data = java.util.HashMap<String, Any>()
                data.put("GithubUser", Gson().toJson(AdapterList[position]).toString())

                (context as Activity).NewIntentWithData(
                    GithubUser::class.java,
                    data,
                    false,
                    false
                )
            }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var lblUserName = view.lblUserName
            var imgAvatar = view.imgAvatar
        }
    }

}
