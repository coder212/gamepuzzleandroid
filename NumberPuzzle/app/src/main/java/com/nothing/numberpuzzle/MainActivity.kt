package com.nothing.numberpuzzle

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.TranslateAnimation
import android.widget.GridView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColor
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.nothing.numberpuzzle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAngka: ArrayList<Int>
    private lateinit var arbaAdapter: ArbaAdapter
    private var posisic = 0
    private var posisizero =0
    private lateinit var correct: ArrayList<Int>
    private var gridenable=false
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).also { binding = it }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            val windowInsets = window.decorView.rootWindowInsets
            if (windowInsets != null) {
                val displayCutout = windowInsets.displayCutout
                if (displayCutout != null) {
                    val safeInsetTop = displayCutout.safeInsetTop
                    val newLayoutParams = binding.root.layoutParams as ViewGroup.MarginLayoutParams
                    newLayoutParams.setMargins(0, safeInsetTop, 0, 0)
                    binding.root.layoutParams = newLayoutParams
                }
            }
        }
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val windowInsetsController: WindowInsetsControllerCompat? =
                ViewCompat.getWindowInsetsController(window.decorView) //gets WindowInsetsController
            windowInsetsController?.hide(WindowInsetsCompat.Type.systemBars()) //Hides system bars (notification bar + navigation bar)

            //Enables Edge 2 Edge, allowing the app window to extend behind the system bars
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
        binding.buttonEnab=true
        listAngka = ArrayList()
        listAngka.add(1)
        listAngka.add(2)
        listAngka.add(3)
        listAngka.add(4)
        listAngka.add(5)
        listAngka.add(6)
        listAngka.add(7)
        listAngka.add(8)
        listAngka.add(0)
        correct = ArrayList()
        correct.addAll(listAngka)
        if(resources.configuration.orientation== Configuration.ORIENTATION_LANDSCAPE){
            arbaAdapter = ArbaAdapter(this, listAngka, true)
        }else {
            arbaAdapter = ArbaAdapter(this, listAngka, false)
        }
        binding.button.setOnClickListener { _ ->
            while (true) {
                listAngka.shuffle()
                //arbaAdapter.notifyDataSetChanged()
                var inversion = 0
                listAngka.forEach { angka ->
                    for (i in (listAngka.indexOf(angka)+1).rangeTo(listAngka.size-1)) if (angka > listAngka[i]&&listAngka[i]!=0) inversion++
                    //if (listAngka[angka] == 0 && angka % 2 == 1) inversion++

                }
                Log.d("posisi","inversinya: $inversion")
                if (inversion % 2 == 0) {
                    Log.d("posisi", "solvable")
                    gridenable = true
                    binding.buttonEnab = false
                    binding.time.base = SystemClock.elapsedRealtime()
                    binding.time.start()
                    arbaAdapter.notifyDataSetChanged()
                    break
                } else {
                    Log.d("posisi", "Not SOlvable")
                }
            }
        }
        binding.gridpuzz.adapter = arbaAdapter
        binding.gridpuzz.setOnTouchListener{v, event ->
            val valueX = event.x.toInt()
            val valuey = event.y.toInt()
            when(event.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    posisizero = listAngka.indexOf(0)
                    posisic = (v as? GridView)?.pointToPosition(valueX, valuey)!!
                    //Log.d("pos","posisi $posisic")
                    if(posisic> -1) {
                        if (gridenable) {
                            if (posisic == 0 && ((posisic + 1) == posisizero || (posisic + 3) == posisizero)) {
                                //Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                val n = listAngka[posisic]
                                listAngka.set(posisizero,n)
                                listAngka.set(posisic, 0)
                                if(posisic+1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideRight()
                                if(posisic+3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideDown()
                                //arbaAdapter.notifyDataSetChanged()
                            } else if (posisic == 1 && ((posisic + 1) == posisizero || (posisic + 3) == posisizero || (posisic - 1) == posisizero)) {
                                //Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                val n = listAngka[posisic]
                                listAngka.set(posisizero,n)
                                listAngka.set(posisic, 0)
                                if(posisic+1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideRight()
                                if(posisic+3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideDown()
                                if(posisic-1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideLeft()
                                //arbaAdapter.notifyDataSetChanged()
                            } else if (posisic == 2 && ((posisic + 3) == posisizero || (posisic - 1) == posisizero)) {
                                //Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                val n = listAngka[posisic]
                                listAngka.set(posisizero,n)
                                listAngka.set(posisic, 0)
                                if(posisic+3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideDown()
                                if(posisic-1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideLeft()
                                //arbaAdapter.notifyDataSetChanged()
                            } else if (posisic == 3 && ((posisic + 1) == posisizero || (posisic + 3) == posisizero || (posisic-3)==posisizero)) {
                               // Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                if (posisic - 1 != posisizero) {
                                    val n = listAngka[posisic]
                                    listAngka.set(posisizero,n)
                                    listAngka.set(posisic, 0)
                                    if(posisic+1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideRight()
                                    if(posisic-3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideUp()
                                    if(posisic+3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideDown()
                                    //arbaAdapter.notifyDataSetChanged()
                                }else{
                                }
                            } else if (posisic == 4 && ((posisic + 1) == posisizero || (posisic + 3) == posisizero || (posisic - 1) == posisizero || (posisic - 3) == posisizero)) {
                                //Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                val n = listAngka[posisic]
                                listAngka.set(posisizero,n)
                                listAngka.set(posisic, 0)
                                if(posisic+1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideRight()
                                if(posisic+3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideDown()
                                if(posisic-1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideLeft()
                                if(posisic-3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideUp()
                                //arbaAdapter.notifyDataSetChanged()
                            } else if (posisic == 5 && ((posisic - 1) == posisizero || (posisic + 3) == posisizero || (posisic - 3) == posisizero)) {
                                //Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                val n = listAngka[posisic]
                                listAngka.set(posisizero,n)
                                listAngka.set(posisic, 0)
                                if(posisic+1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideRight()
                                if(posisic+3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideDown()
                                if(posisic-1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideLeft()
                                if(posisic-3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideUp()
                                //arbaAdapter.notifyDataSetChanged()
                            } else if (posisic == 6 && ((posisic + 1) == posisizero || (posisic - 3) == posisizero)) {
                                //Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                if (posisic - 1 != posisizero) {
                                    val n = listAngka[posisic]
                                    listAngka.set(posisizero,n)
                                    listAngka.set(posisic, 0)
                                    if(posisic+1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideRight()
                                    if(posisic-3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideUp()
                                    //arbaAdapter.notifyDataSetChanged()
                                }
                            } else if (posisic == 7 && ((posisic + 1) == posisizero || (posisic - 1) == posisizero || (posisic - 3) == posisizero)) {
                                //Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                val n = listAngka[posisic]
                                listAngka.set(posisizero,n)
                                listAngka.set(posisic, 0)
                                if(posisic+1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideRight()
                                if(posisic-1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideLeft()
                                if(posisic-3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideUp()
                                //arbaAdapter.notifyDataSetChanged()
                            } else if (posisic == 8 && ((posisic - 1) == posisizero || (posisic - 3) == posisizero)) {
                                //Log.d("posisi","posisi current: $posisic")
                                //Log.d("posisi", "posisi target: $posisizero")
                                val n = listAngka[posisic]
                                listAngka.set(posisizero,n)
                                listAngka.set(posisic, 0)
                                if(posisic-1==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideLeft()
                                if(posisic-3==posisizero) ((v as? GridView)?.getChildAt(posisic) as LinearLayout).slideUp()
                                //arbaAdapter.notifyDataSetChanged()
                            }else{
                                Log.d("posisi","langkah ilegal")
                            }
                            //arbaAdapter.notifyDataSetChanged()
                            v.invalidate()
                            arbaAdapter.notifyDataSetChanged()
                            if(listAngka.equals(correct)){
                                val catatanwaktu = (SystemClock.elapsedRealtime()  - binding.time.base)
                                binding.time.stop()
                                binding.time.base = SystemClock.elapsedRealtime()
                                var detik = Math.floorDiv(catatanwaktu, 1000)
                                var menit = 0L
                                var jam = 0L
                                var bestDetik: Long
                                var bestMenit = 0L
                                var bestJam = 0L
                                if(detik >=60){
                                    menit = Math.floorDiv(detik, 60)
                                    detik = (detik % 60)
                                }
                                if(menit >= 60){
                                    jam = Math.floorDiv(menit, 60)
                                    menit = menit%60
                                }
                                gridenable = false
                                binding.buttonEnab = true
                                var sharedpref = getPreferences(MODE_PRIVATE)
                                val bestValue = sharedpref.getLong(getString(R.string.best), 0L)
                                val alert = AlertDialog.Builder(binding.root.context)
                                alert.setTitle("Catatanmu")
                                if(bestValue == 0L) {
                                    with(sharedpref.edit()) {
                                        putLong(getString(R.string.best), catatanwaktu)
                                        commit()
                                    }
                                }else{
                                    if(catatanwaktu < bestValue){
                                        with(sharedpref.edit()){
                                            putLong(getString(R.string.best), catatanwaktu)
                                            commit()
                                        }
                                    }
                                }
                                if((bestValue == 0L) || (bestValue > catatanwaktu)) {
                                    alert.setMessage(
                                        "Kamu menyelesaikan puzzle ini dalam %02d:%02d:%02d\ncatatan terbaikmu %02d:%02d:%02d".format(
                                            jam,
                                            menit,
                                            detik,
                                            jam,
                                            menit,
                                            detik
                                        )
                                    )
                                }else{
                                    bestDetik = Math.floorDiv(bestValue, 1000)
                                    if(bestDetik >= 60){
                                        bestMenit = Math.floorDiv(bestDetik, 60)
                                        bestDetik = bestDetik % 60
                                    }
                                    if(bestMenit >= 60){
                                        bestJam = Math.floorDiv(bestMenit, 60)
                                        bestMenit = bestMenit % 60
                                    }
                                    alert.setMessage(
                                        "Kamu menyelesaikan puzzle ini dalam %02d:%02d:%02d\ncatatan terbaikmu %02d:%02d:%02d".format(
                                            jam,
                                            menit,
                                            detik,
                                            bestJam,
                                            bestMenit,
                                            bestDetik
                                        )
                                    )
                                }
                                alert.setNeutralButton("OK", DialogInterface.OnClickListener{ dialog, _ ->

                                    dialog.cancel()
                                })
                                alert.setCancelable(false)
                                alert.create()
                                alert.show()
                            }
                        }
                    }
                }
            }
            true
        }
    }
    fun View.slideUp(duration: Int = 100) {
        //visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    fun View.slideDown(duration: Int = 100) {
        //visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }
    fun View.slideLeft(duration: Int = 100) {
        //visibility = View.VISIBLE
        val animate = TranslateAnimation(this.width.toFloat(), 0f, 0f, 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }
    fun View.slideRight(duration: Int = 100) {
        //visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, this.width.toFloat(), 0f, 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

}