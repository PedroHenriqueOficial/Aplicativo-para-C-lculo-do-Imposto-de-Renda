package com.example.impostoderenda

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impostoderenda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding;

    private val DEDUCAO_POR_DEPENDENTE = 189.59 // Dedução por dependente

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCalcular.setOnClickListener(this)

        Toast.makeText(applicationContext, R.string.calculo_imposto_renda, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View) {
        if(view.id == binding.buttonCalcular.id) {
            val resultado = "Imposto a pagar : R$ ${String.format("%.2f", calcularImposto())}"
            binding.resultado.text = resultado
        }
    }

    private fun calcularImposto() : Double{

        val salarioString = binding.salario.text.toString()
        val gastosString = binding.gastos.text.toString()
        val dependentesString = binding.dependentes.text.toString()

        if(salarioString.isEmpty() || gastosString.isEmpty() || dependentesString.isEmpty()) { // Verifica se há algum valor vazio
            toast("Preencha todos os campos")
            return 0.0
        }

        var imposto = 0.0

        val salario = salarioString.toDoubleOrNull() ?: 0.0
        val gastos = gastosString.toDoubleOrNull() ?: 0.0
        val dependentes = dependentesString.toIntOrNull() ?: 0

        val gastosDependentes = dependentes * DEDUCAO_POR_DEPENDENTE
        val calculo = salario - gastosDependentes - gastos

        when {

            calculo <= 2428.80 -> { // Alíquota de 0.0%
                imposto = 0.0
            }

            (calculo in 2428.81..2826.65) -> { // Alíquota de 7.5%
                imposto = (calculo * 0.075) - 182.16
            }

            (calculo in 2826.66..3751.05) -> { // Alíquota de 15.0%
                imposto = (calculo * 0.15) - 394.16
            }

            (calculo in 3751.06..4664.68) -> { // Alíquota de 22.5%
                imposto = (calculo * 0.225) - 675.49
            }

            calculo > 4664.68 -> { // Alíquota de 27.5%
                imposto = (calculo * 0.275) - 908.73
            }
        }

        if(imposto < 0.0) { // Garante que o imposto não será negativo
            imposto = 0.0
        }

        return imposto
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}