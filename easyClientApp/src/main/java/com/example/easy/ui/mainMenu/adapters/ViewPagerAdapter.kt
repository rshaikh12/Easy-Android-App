package com.example.easy.ui.mainMenu.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.easy.ui.mainMenu.fragments.*
/**
 * @author Roxana Shaikh
 */
class ViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return   when(position){
            0->{
                MapFragment()
            }
            1->{
                SelectedUserItemsFragment()
            }
            2->{
                ItemFragment()
            }
            3->{
                AllChatsFragment()
            }
            else->{
                Fragment()
            }

        }
    }
}