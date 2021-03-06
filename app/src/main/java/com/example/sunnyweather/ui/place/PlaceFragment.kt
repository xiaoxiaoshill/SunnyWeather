package com.example.sunnyweather.ui.place

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.MainActivity
import com.example.sunnyweather.R
import com.example.sunnyweather.ui.weather.WeatherActivity

class PlaceFragment: Fragment() {

    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter:PlaceAdapter
    private lateinit var recyclerview:RecyclerView
    private lateinit var searchPlaceEdit:EditText
    private lateinit var baImageView:ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_place,container,false)
        recyclerview = view.findViewById(R.id.recyclerView)
        searchPlaceEdit = view.findViewById(R.id.searchPlaceEdit)
        baImageView = view.findViewById(R.id.baImageView)
        return view
    }

    @SuppressLint("NotifyDataSetChanged", "FragmentLiveDataObserve")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is MainActivity && viewModel.isPlaceSaved()){
            val place = viewModel.getSavePlace()
            val intent = Intent(context,WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }


        val layoutManager = LinearLayoutManager(activity)
        recyclerview.layoutManager = layoutManager
        adapter = PlaceAdapter(this,viewModel.placeList)
        recyclerview.adapter = adapter
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                recyclerview.visibility = View.GONE
                baImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(this, Observer { result ->
            val places = result.getOrNull()
            if (places != null){
                recyclerview.visibility = View.VISIBLE
                baImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"???????????????????????????",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}