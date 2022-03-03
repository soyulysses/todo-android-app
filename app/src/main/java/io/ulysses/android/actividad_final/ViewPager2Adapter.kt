package io.ulysses.android.actividad_final

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    /**
     *
     * */
    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    /**
     * Crea los fragments
     * */
    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

    /**
     * Guarda los fragments y los titulos en [mFragmentList] y [mFragmentTitleList]
     * */
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    /**
     * Devuelte el título
     * */
    fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}