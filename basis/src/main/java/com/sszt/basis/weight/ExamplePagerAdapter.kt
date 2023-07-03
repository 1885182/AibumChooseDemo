package com.sszt.basis.weight


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 *
 * @param time 2021/7/22
 * @param des ViewPage的Adapter
 * @param author Meng
 *
 */
class ExamplePagerAdapter(fm: FragmentManager,val mFragments: List<Fragment>,val mTitle:List<String>) :
    FragmentPagerAdapter(fm) {
     override fun getItem(position: Int): Fragment { //必须实现
        return mFragments[position]
    }

    override fun getCount(): Int { //必须实现
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence { //选择性实现
        return mTitle[position]
    }

}
