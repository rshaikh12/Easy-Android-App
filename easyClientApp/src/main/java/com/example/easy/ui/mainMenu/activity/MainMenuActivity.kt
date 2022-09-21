package com.example.easy.ui.mainMenu.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.easy.R
import com.example.easy.ui.mainMenu.AddNewItemActivity
import com.example.easy.ui.mainMenu.ProfileActivity
import com.example.easy.ui.mainMenu.adapters.AdAdapter
import com.example.easy.ui.mainMenu.adapters.SelectedUserAdsAdapter
import com.example.easy.ui.mainMenu.adapters.AllChatsAdapter
import com.example.easy.ui.mainMenu.adapters.ViewPagerAdapter
import com.example.easy.ui.mainMenu.fragments.ItemFragment
import com.example.easy.ui.viewmodels.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author Roxana Shaikh, Marius Funk
 */
class MainMenuActivity : AppCompatActivity(), AdapterView.OnItemClickListener  {




    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setup viewmodel with user
        viewModel = ViewModelProvider(this, MainActivityViewModelFactory())
            .get(MainActivityViewModel::class.java)

        viewModel.setUser(intent.getStringExtra("USER")!!)

        setContentView(R.layout.main_menu)

        //Navigation for the top bar
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController




        val tabLayout=findViewById<TabLayout>(R.id.tab_layout)
        val viewPager2=findViewById<ViewPager2>(R.id.view_pager_2)

        val adapter= ViewPagerAdapter(supportFragmentManager,lifecycle)



        viewPager2.adapter=adapter

        //Defines tab strings
        TabLayoutMediator(tabLayout,viewPager2){tab,position->
            when(position){
                0->{
                    tab.text="Map"
                }
                1->{
                    tab.text="Selected User Ads"
                }
                2->{
                    tab.text="My Ads"
                }
                3->{
                    tab.text="My Chats"
                }
            }
        }.attach()

    }


    //Opens selected options, in this case only profile (NOT FINISHED)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_profile  -> openProfileActivity()
        }

        return super.onOptionsItemSelected(item)

    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header, menu)
        return true
    }

    fun dataChanged(adapter: RecyclerView.Adapter<AdAdapter.ViewHolder>){
        adapter.notifyDataSetChanged()

    }

    fun allDataChanged(adapter: RecyclerView.Adapter<SelectedUserAdsAdapter.ViewHolder>){
        adapter.notifyDataSetChanged()

    }
    fun allChatDataChanged(adapter: RecyclerView.Adapter<AllChatsAdapter.ViewHolder>){
        adapter.notifyDataSetChanged()

    }

    private fun openProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    //UNUSED
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

}
