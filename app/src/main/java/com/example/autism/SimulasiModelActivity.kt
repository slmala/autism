package com.example.autism

import android.annotation.SuppressLint
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SimulasiModelActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private val mModelPath = "autis.tflite"

    private lateinit var resultText: TextView
    private lateinit var A1_Score: EditText
    private lateinit var A2_Score: EditText
    private lateinit var A3_Score: EditText
    private lateinit var A4_Score: EditText
    private lateinit var A5_Score: EditText
    private lateinit var A6_Score: EditText
    private lateinit var A7_Score: EditText
    private lateinit var A8_Score: EditText
    private lateinit var A9_Score: EditText
    private lateinit var A10_Score: EditText
    private lateinit var checkButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulasi_model)

        resultText = findViewById(R.id.txtResult)
        A1_Score = findViewById(R.id.A1_Score)
        A2_Score = findViewById(R.id.A2_Score)
        A3_Score = findViewById(R.id.A3_Score)
        A4_Score = findViewById(R.id.A4_Score)
        A5_Score = findViewById(R.id.A5_Score)
        A6_Score = findViewById(R.id.A6_Score)
        A7_Score = findViewById(R.id.A7_Score)
        A8_Score = findViewById(R.id.A8_Score)
        A9_Score = findViewById(R.id.A9_Score)
        A10_Score = findViewById(R.id.A10_Score)
        checkButton = findViewById(R.id.btnCheck)

        checkButton.setOnClickListener {
            var result = doInference(
                A1_Score.text.toString(),
                A2_Score.text.toString(),
                A3_Score.text.toString(),
                A4_Score.text.toString(),
                A5_Score.text.toString(),
                A6_Score.text.toString(),
                A7_Score.text.toString(),
                A8_Score.text.toString(),
                A9_Score.text.toString(),
                A10_Score.text.toString())
            runOnUiThread {
                if (result == 0) {
                    resultText.text = "Yes"
                }else if (result == 1){
                    resultText.text = "No"
                }
            }
        }
        initInterpreter()
    }

    private fun initInterpreter() {
        val options = org.tensorflow.lite.Interpreter.Options()
        options.setNumThreads(7)
        options.setUseNNAPI(true)
        interpreter = org.tensorflow.lite.Interpreter(loadModelFile(assets, mModelPath), options)
    }

    private fun doInference(input1: String, input2: String, input3: String, input4: String, input5: String, input6: String, input7: String, input8: String, input9: String, input10: String): Int{
        val inputVal = FloatArray(10)
        inputVal[0] = input1.toFloat()
        inputVal[1] = input2.toFloat()
        inputVal[2] = input3.toFloat()
        inputVal[3] = input4.toFloat()
        inputVal[4] = input5.toFloat()
        inputVal[5] = input6.toFloat()
        inputVal[6] = input7.toFloat()
        inputVal[7] = input8.toFloat()
        inputVal[8] = input9.toFloat()
        inputVal[9] = input10.toFloat()
        val output = Array(1) { FloatArray(2) }
        interpreter.run(inputVal, output)

        Log.e("result", (output[0].toList()+" ").toString())

        return output[0].indexOfFirst { it == output[0].maxOrNull() }
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer{
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}