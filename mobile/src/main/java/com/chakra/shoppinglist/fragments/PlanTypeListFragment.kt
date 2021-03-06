package com.chakra.shoppinglist.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chakra.shoppinglist.R
import com.chakra.shoppinglist.base.BaseFragment
import com.chakra.shoppinglist.model.ShoppingPlanType
import com.chakra.shoppinglist.utils.closeKeyBoard
import com.chakra.shoppinglist.viewmodel.CommonViewModel
import com.chakra.shoppinglist.viewmodel.PlanTypeListViewModel
import kotlinx.android.synthetic.main.fragment_add_plan_layout.*
import kotlinx.android.synthetic.main.fragment_planner_list_layout.planList
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanTypeListFragment : BaseFragment() {
    companion object {
        private const val PLAN_LIST_COUNT_EXTRA = "plan_list_count"

        fun getDataBundle(noOfPlansCount: Int) = Bundle().apply {
            putInt(PLAN_LIST_COUNT_EXTRA, noOfPlansCount)
        }
    }

    private var backPressCallback: OnBackPressedCallback? = null
    override val resourceLayoutId: Int
        get() = R.layout.fragment_add_plan_layout

    private val viewModel: PlanTypeListViewModel by viewModel()
    private val commonViewModel: CommonViewModel by sharedViewModel()

    private lateinit var adapter: PlanTypeListAdapter

    override fun getBaseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.let {
                viewModel.noOfPlans = it.getInt(PLAN_LIST_COUNT_EXTRA)
            }
        }
        if (viewModel.noOfPlans == 0) {
            backPressCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback!!)
        }
    }

    override fun initialize() {
        val layoutManager = GridLayoutManager(context, 2)
        planList.layoutManager = layoutManager
        adapter = PlanTypeListAdapter()
        planList.adapter = adapter

        addButton.setOnClickListener {
            onAddPlanWithName(planNameEditText.text.toString())
        }

        commonViewModel.color.value?.let {
            addButton.setBackgroundColor(Color.parseColor(it))
        }

        planNameEditText.setOnEditorActionListener { _: TextView, actionId: Int, event: KeyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onAddPlanWithName(planNameEditText.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        viewModel.shoppingTypeSuggestions.observe(viewLifecycleOwner) { list ->
            adapter.setList(list)
        }

        viewModel.shoppingPlanAddedLiveData.observe(viewLifecycleOwner) { shoppingPlan ->
            shoppingPlan?.let {
                commonViewModel.loadShoppingPlan(it)
                backPressCallback?.isEnabled = false
                moveToPreviousScreen()
                findNavController().navigate(R.id.action_planListScreen_to_shoppingCartViewScreen)
            }
        }

        planNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                text?.let {
                    viewModel.filterSuggestions(it.toString())
                } ?: viewModel.filterSuggestions(null)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Ignore
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Ignore
            }
        })
    }

    override fun getTitle() = getString(R.string.choose_a_plan)

    private fun onAddPlanWithName(planName: String) {
        if (planName.isNotBlank()) {
            closeKeyBoard()
            viewModel.addPlan(planName, null)
        }
    }

    private fun onAddPlan(planType: ShoppingPlanType) {
        viewModel.addPlan(planType.name, planType)
    }

    inner class PlanTypeListAdapter : RecyclerView.Adapter<ViewHolder>() {
        private var planTypeList: List<ShoppingPlanType>? = null
        private val imageLoader = Glide.with(context!!)

        fun setList(list: List<ShoppingPlanType>?) {
            this.planTypeList = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.layout_shopping_plan_add_list_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(planTypeList!![position], imageLoader)
        }

        override fun getItemCount() = planTypeList?.size ?: 0
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cardView: CardView = view.findViewById(R.id.cardView)
        var background: ImageView = view.findViewById(R.id.background)
        var label: TextView = view.findViewById(R.id.label)

        fun bind(planType: ShoppingPlanType, imageLoader: RequestManager) {
            itemView.setOnClickListener {
                onAddPlan(planType)
            }
            planType.image?.let {
                imageLoader.load(it).into(background)
            }
            label.text = planType.name
        }
    }
}