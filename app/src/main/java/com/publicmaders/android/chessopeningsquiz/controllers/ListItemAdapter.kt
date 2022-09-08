package com.publicmaders.android.chessopeningsquiz.controllers

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.*
import com.publicmaders.android.chessopeningsquiz.R
import com.publicmaders.android.chessopeningsquiz.models.Opening
import java.util.*

class ListItemAdapter(private val context: Context, private val openingList: MutableList<Opening>) :
    BaseAdapter(), Filterable
{
    private lateinit var openingName: TextView
    private lateinit var lockImage: ImageView
    private var itemsModelListFiltered = openingList

    override fun getCount(): Int
    {
        return itemsModelListFiltered.size
    }

    override fun getItem(position: Int): Opening
    {
        return itemsModelListFiltered[position]
    }

    override fun getItemId(position: Int): Long
    {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View?
    {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        openingName = convertView.findViewById(R.id.tvOpening)
        openingName.text = itemsModelListFiltered[position].toString()

        lockImage = convertView.findViewById(R.id.ivLock)
        if (!itemsModelListFiltered[position].IsLocked())
        {
            lockImage.visibility = INVISIBLE
        }

        return convertView
    }


    override fun getFilter(): Filter?
    {
        return object : Filter()
        {
            override fun performFiltering(constraint: CharSequence): FilterResults
            {
                val filterResults = FilterResults()
                if (constraint.isEmpty())
                {
                    filterResults.count = openingList.size
                    filterResults.values = openingList
                }
                else
                {
                    val resultsModel: MutableList<Opening> = ArrayList()
                    val searchStr = constraint.toString().lowercase(Locale.getDefault())
                    for (itemsModel in openingList)
                    {
                        if (itemsModel.toString().lowercase(Locale.getDefault())
                                .contains(searchStr))
                        {
                            resultsModel.add(itemsModel)
                        }
                        filterResults.count = resultsModel.size
                        filterResults.values = resultsModel
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults)
            {
                itemsModelListFiltered = results.values as MutableList<Opening>
                notifyDataSetChanged()
            }
        }
    }
}
