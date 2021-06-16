package hani.momanii.supernova_emoji_library.gif

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import hani.momanii.supernova_emoji_library.R
import retrofit2.Call
import retrofit2.Callback
import java.util.*

open class SearchGifFragment : BottomSheetDialogFragment() {

    private var adapter: GifSearchAdapter? = null
    private var recycleView: RecyclerView? = null
    private var edtSearch: EditText? = null
    private var tilSearch: TextInputLayout? = null
    private var data: MutableList<MediaItem> = mutableListOf()
    private var callback: SearchGifFragmentCallback? = null

    private var timer: Timer? = null
    private val delay: Long = 1000

    val q: String
        get() = edtSearch?.text.toString()

    fun show(fragmentManager: FragmentManager, callback: SearchGifFragmentCallback) {
        val dialog = SearchGifFragment()
        dialog.callback = callback
        dialog.show(fragmentManager, "SearchGifFragment")
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search_gif, container, false)

        recycleView = view.findViewById(R.id.rvGif)
        edtSearch = view.findViewById(R.id.edtSearch)
        tilSearch = view.findViewById(R.id.tilSearch)
        adapter = GifSearchAdapter(data) {
            callback?.onContentClicked(it.tinygif!!.url!!)
            dismiss()
        }
        val layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        recycleView?.layoutManager = layoutManager
        recycleView?.adapter = adapter

        tilSearch?.setStartIconOnClickListener {
            dismiss()
        }

        edtSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                timer?.cancel()
                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        get(q)
                    }

                }, delay)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        get("excited")

        edtSearch?.isFocusableInTouchMode = true
        edtSearch?.requestFocus()
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(tilSearch, InputMethodManager.SHOW_IMPLICIT)

        getTrending()

        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

    }

    interface SearchGifFragmentCallback {
        fun onContentClicked(url: String)
    }

    private fun get(q: String) {
        data.clear()
        val client = Client()
        val call = client.makeService().searchGift(q)
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val body = response.body()?.results
                    for (i in 0 until body?.size!!) {
                        body[i].media?.let { data.addAll(it) }
                    }
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}", t)
            }

        })
    }

    private fun getTrending() {
        data.clear()
        val client = Client()
        val call = client.makeService().trendingGift()
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val body = response.body()?.results
                    for (i in 0 until body?.size!!) {
                        body[i].media?.let { data.addAll(it) }
                    }
                    adapter?.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}", t)
            }

        })
    }
}