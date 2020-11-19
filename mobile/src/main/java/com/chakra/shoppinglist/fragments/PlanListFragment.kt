package com.chakra.shoppinglist.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.ShoppingPlanWithType
import com.chakra.shoppinglist.utils.WearableService
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import com.chakra.shoppinglist.viewmodel.PlanListViewModel
import kotlinx.android.synthetic.main.fragment_planner_list_layout.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanListFragment : BaseFragment() {
    private val viewModel: PlanListViewModel by viewModel()
    val commonViewModel: CommonViewModel by sharedViewModel()

    private lateinit var adapter: PlanListAdapter
    override val resourceLayoutId: Int = R.layout.fragment_planner_list_layout

    override fun getBaseViewModel() = viewModel

    override fun initialize() {
        // Start wearable service
        val intent = Intent(context, WearableService::class.java)
        // TODO: start Wearable service
        //requireActivity().startService(intent)

        val layoutManager = LinearLayoutManager(context)
        planList.layoutManager = layoutManager
        adapter = PlanListAdapter()
        planList.adapter = adapter

        viewModel.shoppingListLiveData.observe(viewLifecycleOwner) { list ->
            list?.let {
                if (it.isEmpty()) {
                    findNavController().navigate(R.id.action_planListScreen_to_addPlanScreen, PlanTypeListFragment.getDataBundle(0))
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

    override fun getTitle() = getString(R.string.choose_a_plan)

    override fun onStart() {
        super.onStart()
        commonViewModel.clearShoppingPlanSelection()
    }

    inner class PlanListAdapter : RecyclerView.Adapter<ViewHolder>() {
        private var planList: List<ShoppingPlanWithType>? = null
        private val imageLoader = Glide.with(context!!)
        fun setList(list: List<ShoppingPlanWithType>?) {
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
        var image: ImageView = view.findViewById(R.id.image)
        var checkBox: CheckBox = view.findViewById(R.id.checkBox)
        var label: TextView = view.findViewById(R.id.label)
        var count: TextView = view.findViewById(R.id.count)
        var progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        fun bind(planData: ShoppingPlanWithType, imageLoader: RequestManager) {
            itemView.setOnClickListener {
                commonViewModel.loadShoppingPlan(planData)
                findNavController().navigate(R.id.action_planListScreen_to_shoppingCartViewScreen)
            }
            planData.planType?.image?.let {
                imageLoader.load(it).into(image)
            }
            planData.shoppingPlan.apply {
                checkBox.isChecked = totalItems > 0 && doneCount == totalItems
            }

            planData.shoppingPlan.let {
                count.text = "${it.doneCount}/${it.totalItems}"
                if (it.totalItems == 0) {
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.VISIBLE
                    progressBar.progress = (it.doneCount * 100 / it.totalItems)
                }
            }

            label.text = planData.shoppingPlan.name
        }
    }
}