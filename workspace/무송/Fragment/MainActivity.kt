package com.example.healthmyself.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.healthmyself.Adapter.AdapterMainFragment
import com.example.healthmyself.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureBottonNavigation()
    }
    private fun configureBottonNavigation() {
        xml_main_viewpager.adapter = AdapterMainFragment(supportFragmentManager, 5)
        xml_main_tablayout.setupWithViewPager(xml_main_viewpager)

        val viewBtmNaviMain : View = this.layoutInflater.inflate(R.layout.btm_navigation_main, null, false)
        xml_main_tablayout.getTabAt(0)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_timer)
        xml_main_tablayout.getTabAt(1)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_calendar)
        xml_main_tablayout.getTabAt(2)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_main)
        xml_main_tablayout.getTabAt(3)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_video)
        xml_main_tablayout.getTabAt(4)!!.customView = viewBtmNaviMain.findViewById(R.id.xml_btmnv_btn_setting)

        xml_main_viewpager.setOffscreenPageLimit(5);
        //AdpapterMainFragment쪽에서 상태유지를 한다고 해도 작동중이 아닌 상태에서는 다시 생성하는 경우가 발생
        //따라서 viewpager의 최대수를 5로 제한하여 프래그먼트 5개모두 데이터를 유지 할 수 있도록 최대치를 설정함
        //결론적으로 모든 프래그먼트를 재사용하게 됨.
    }
    fun selectFragment(position : Int) {
        xml_main_viewpager.setCurrentItem(position, true)
    }
}