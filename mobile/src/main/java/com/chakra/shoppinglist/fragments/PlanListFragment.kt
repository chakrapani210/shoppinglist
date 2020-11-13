package com.chakra.shoppinglist.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.ShoppingPlanCartListItemData
import com.chakra.shoppinglist.utils.WearableService
import com.chakra.shoppinglist.viewmodel.PlanListViewModel
import kotlinx.android.synthetic.main.fragment_planner_list_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanListFragment : BaseFragment() {
    private val viewModel: PlanListViewModel by viewModel()
    private lateinit var adapter: PlanListAdapter
    override val resourceLayoutId: Int = R.layout.fragment_planner_list_layout

    override fun getBaseViewModel() = viewModel

    override fun initialize() {
        // Start wearable service
        val intent = Intent(context, WearableService::class.java)
        // TODO: start Wearable service
        //requireActivity().startService(intent)

        val layoutManager = GridLayoutManager(context, 2)
        planList.layoutManager = layoutManager
        adapter = PlanListAdapter()
        planList.adapter = adapter

        viewModel.shoppingListLiveData.observe(viewLifecycleOwner) { list ->
            list?.let {
                if (it.isEmpty()) {
                    findNavController().navigate(R.id.action_planListScreen_to_addPlanScreen)
                } else {
                    adapter.setList(it)
                }
            }
        }
    }

    override fun onFloatingButtonClicked() {
        findNavController().navigate(R.id.action_planListScreen_to_addPlanScreen)
    }

    override fun isFloatingButtonEnabled() = true

    override fun getTitle() = getString(R.string.shopping_plan_list_label)

    inner class PlanListAdapter : RecyclerView.Adapter<ViewHolder>() {
        private var planList: List<ShoppingPlanCartListItemData>? = null
        private val imageLoader = Glide.with(context!!)
        fun setList(list: List<ShoppingPlanCartListItemData>?) {
            this.planList = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_shopping_plan_list_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(planList!![position], imageLoader)
        }

        override fun getItemCount() = planList?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: CardView = view.findViewById(R.id.cardView)
        var background: ImageView = view.findViewById(R.id.background)
        var checkBox: CheckBox = view.findViewById(R.id.checkBox)
        var label: TextView = view.findViewById(R.id.label)

        fun bind(planData: ShoppingPlanCartListItemData, imageLoader: RequestManager) {
            itemView.setOnClickListener {
                findNavController().navigate(R.id.action_planListScreen_to_shoppingCartViewScreen,
                        ShoppingCartViewFragment.getDataBundle(planData.shoppingPlan))
            }
            planData.planType.image?.let {
                imageLoader.load(it).into(background)
            }
            planData.inCartProductCountData.apply {
                checkBox.isChecked = (doneCount == totalItems)
            }

            label.text = planData.shoppingPlan.name
        }
    }
}