package com.example.recipeapp.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R

class InstructionsAdapter(private var steps: List<InstructionStep>) :
    RecyclerView.Adapter<InstructionsAdapter.InstructionViewHolder>() {

    inner class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStepNumber: TextView = itemView.findViewById(R.id.stepNumber)
        val tvInstructionText: TextView = itemView.findViewById(R.id.stepInstruction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_instruction, parent, false)
        return InstructionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val step = steps[position]
        holder.tvStepNumber.text = "${position + 1}"
        holder.tvInstructionText.text = step.step.trim()
    }

    fun updateList(newList: List<InstructionStep>) {
        this.steps = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = steps.size
}
