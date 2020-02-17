package com.bunizz.instapetts.utils.imagePicker.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bunizz.instapetts.R
import com.bunizz.instapetts.utils.imagePicker.helper.ExtraName


class DetailActivity : AppCompatActivity() {
    private lateinit var fragment: DetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagepicker_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val path = intent.getStringExtra(ExtraName.IMAGE_PATH.name)
            fragment = DetailFragment.newInstance(path)
            supportFragmentManager.beginTransaction().add(R.id.detail_content, fragment).commit()
        } else {
            fragment = supportFragmentManager.findFragmentById(R.id.detail_content) as DetailFragment
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
