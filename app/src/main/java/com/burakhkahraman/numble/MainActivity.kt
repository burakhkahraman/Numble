package com.burakhkahraman.numble

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var guessesContainer: LinearLayout
    private lateinit var newGameButton: Button
    private var secretNumber: String = ""
    private var attempts: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        guessesContainer = findViewById(R.id.guessesContainer)
        newGameButton = findViewById(R.id.newGameButton)

        newGameButton.setOnClickListener {
            resetGame()
        }

        startNewGame()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Ana menüye dönmek için
        startEntryActivity()
    }

    private fun startNewGame() {
        secretNumber = generateNumber()
        attempts = 0
        guessesContainer.removeAllViews()
        addNewGuessRow()
        newGameButton.visibility = Button.GONE
    }

    private fun resetGame() {
        startNewGame()
    }

    private fun addNewGuessRow() {
        val guessRow = LinearLayout(this)
        guessRow.orientation = LinearLayout.HORIZONTAL
        guessRow.setPadding(0, 8, 0, 8)

        val editText1 = createEditText()
        val editText2 = createEditText()
        val editText3 = createEditText()
        val feedbackTextView = createFeedbackTextView()

        setupEditTextNavigation(editText1, editText2)
        setupEditTextNavigation(editText2, editText3, editText1) // Pass previousEditText
        setupEditTextNavigation(editText3, feedbackTextView, editText2) // For last EditText

        editText3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    val guess = "${editText1.text}${editText2.text}${editText3.text}"
                    if (isValidGuess(guess)) {
                        checkGuess(editText1.text.toString(), editText2.text.toString(), editText3.text.toString(), feedbackTextView)
                    } else {
                        Toast.makeText(this@MainActivity, "Lütfen geçerli bir sayı giriniz.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        guessRow.addView(editText1)
        guessRow.addView(editText2)
        guessRow.addView(editText3)
        guessRow.addView(feedbackTextView)

        guessesContainer.addView(guessRow)

        // Focus the first EditText of the new row
        editText1.requestFocus()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun createEditText(): EditText {
        val editText = EditText(this)
        val size = dpToPx(80) // Kutucuk boyutlarını artır
        val params = LinearLayout.LayoutParams(size, size) // en/boy oranını koruyacak şekilde boyutlandır
        params.setMargins(dpToPx(8), 0, dpToPx(8), 0) // kutucuklar arası mesafeyi artır
        editText.layoutParams = params
        editText.maxLines = 1
        editText.textSize = 24f // Yazı boyutunu artır
        editText.isSingleLine = true
        editText.gravity = android.view.Gravity.CENTER // metni ortalayın
        editText.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        editText.background = ContextCompat.getDrawable(this, R.drawable.edit_text_border)
        editText.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        return editText
    }

    private fun createFeedbackTextView(): TextView {
        val feedbackTextView = TextView(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(16, 0, 0, 0)
        feedbackTextView.layoutParams = params
        feedbackTextView.textSize = 18f
        return feedbackTextView
    }

    private fun setupEditTextNavigation(currentEditText: EditText, nextEditText: TextView, previousEditText: EditText? = null) {
        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.isNotEmpty() == true) {
                    nextEditText.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        currentEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && currentEditText.text.isEmpty()) {
                previousEditText?.requestFocus()
                true
            } else {
                false
            }
        }
    }

    private fun checkGuess(digit1: String, digit2: String, digit3: String, feedbackTextView: TextView) {
        val guess = "$digit1$digit2$digit3"
        val (correctPlace, correctDigits) = evaluateGuess(secretNumber, guess)

        val feedback = buildString {
            if (correctPlace > 0) append("+$correctPlace ")
            if (correctDigits > 0) append("-$correctDigits ")
            if (correctPlace == 0 && correctDigits == 0) append("0 ")
        }
        feedbackTextView.text = feedback.trim()

        if (correctPlace == 3) {
            Toast.makeText(this@MainActivity, "Tebrikler! Doğru sayıyı buldunuz.", Toast.LENGTH_LONG).show()
            newGameButton.visibility = Button.VISIBLE
            disableGuessFields() // Tahmin alanını pasif hale getir
        } else {
            addNewGuessRow() // doğru tahmin edilmediyse yeni tahmin satırı eklenir
        }
    }

    private fun disableGuessFields() {
        // Tahmin alanını pasif hale getir
        for (i in 0 until guessesContainer.childCount) {
            val guessRow = guessesContainer.getChildAt(i) as LinearLayout
            for (j in 0 until guessRow.childCount) {
                val view = guessRow.getChildAt(j)
                if (view is EditText) {
                    view.isEnabled = false
                }
            }
        }
    }

    private fun generateNumber(): String {
        var number: String
        do {
            number = (100..999).random().toString()
        } while (!isValidNumber(number))
        return number
    }

    private fun isValidNumber(number: String): Boolean {
        val digits = number.toCharArray()
        return digits.distinct().size == 3
    }

    private fun isValidGuess(guess: String): Boolean {
        return guess.length == 3 && guess.toCharArray().distinct().size == 3
    }

    private fun evaluateGuess(secretNumber: String, guess: String): Pair<Int, Int> {
        val secretDigits = secretNumber.toCharArray()
        val guessDigits = guess.toCharArray()

        var correctPlace = 0
        var correctDigits = 0

        for (i in secretDigits.indices) {
            if (secretDigits[i] == guessDigits[i]) {
                correctPlace++
            } else if (secretDigits.contains(guessDigits[i])) {
                correctDigits++
            }
        }
        return Pair(correctPlace, correctDigits)
    }

    private fun startEntryActivity() {
        val intent = Intent(this, EntryActivity::class.java)
        startActivity(intent)
        finish() // Eğer geri tuşuna basıldığında ana menüye dönersek, oyun ekranını kapatmak için finish() metodunu kullanabiliriz
    }
}
