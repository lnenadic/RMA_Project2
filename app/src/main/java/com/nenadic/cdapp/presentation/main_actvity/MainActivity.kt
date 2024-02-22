package com.nenadic.cdapp.presentation.main_actvity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.nenadic.cdapp.databinding.ActivityMainBinding
import com.nenadic.cdapp.domian.interfaces.OnItemClick
import com.nenadic.cdapp.domian.model.CD
import com.nenadic.cdapp.domian.utils.CDAdapter
import com.nenadic.cdapp.domian.utils.constants.ALBUM_NAME
import com.nenadic.cdapp.domian.utils.constants.ARTIST_NAME
import com.nenadic.cdapp.domian.utils.constants.CD_ID
import com.nenadic.cdapp.domian.utils.constants.DATE
import com.nenadic.cdapp.domian.utils.constants.IMAGE
import com.nenadic.cdapp.domian.utils.constants.ORIGIN
import com.nenadic.cdapp.domian.utils.constants.formatDateFromLong
import com.nenadic.cdapp.presentation.add_edit_actvity.AddEditActivity
import com.nenadic.cdapp.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    @Inject
    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lifecycleScope.launch {
            viewModel.maState
                .collectLatest { list ->
                    setListAdapter(list)
                }
        }
        clicks()
    }

    private fun setListAdapter(list: List<CD>) {
        with(binding) {
            val adapter = CDAdapter(
                list, this@MainActivity
            )
            adapter.initConstructor(object : OnItemClick {
                override fun onDeleteClick(cd: CD) {
                    viewModel.deleteCD(cd)
                    Toast.makeText(this@MainActivity, "Delete Successfully", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onEditClick(cd: CD) {
                    val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                    intent.putExtra(CD_ID, cd.id)
                    intent.putExtra(IMAGE, cd.picture)
                    intent.putExtra(ALBUM_NAME, cd.albumName)
                    intent.putExtra(ARTIST_NAME, cd.artistName)
                    intent.putExtra(DATE, formatDateFromLong(cd.releaseDate))
                    intent.putExtra(ORIGIN, cd.labelOrigin)
                    activityLauncher.launch(intent)
                }
            })
            cdList.adapter = adapter
        }
    }

    private fun clicks() {
        with(binding) {
            addCd.setOnClickListener {
                val intent = Intent(this@MainActivity, AddEditActivity::class.java)
                activityLauncher.launch(intent)
            }
        }
    }

    override fun callBackPress() {
        finish()
    }
}