package br.com.francielilima.githubfetch.extensions

import android.view.View
import android.widget.TextView

fun TextView.setVisibility(condition: Boolean = true) {
    this.visibility = if (condition) View.VISIBLE else View.GONE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}