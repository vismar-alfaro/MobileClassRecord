package ph.kodego.alfaro.vismarjay.firebasedemoapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ph.kodego.alfaro.vismarjay.firebasedemoapp.R
import ph.kodego.alfaro.vismarjay.firebasedemoapp.models.EmployeeModel

class InsertionActivity : AppCompatActivity() {

    private lateinit var etEmpName: EditText
    private lateinit var etEmpAge: EditText
    private lateinit var etEmpSalary: EditText
    private lateinit var save: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etEmpName = findViewById(R.id.etEmpName)
        etEmpAge = findViewById(R.id.etEmpAge)
        etEmpSalary = findViewById(R.id.etEmpSalary)
        save = findViewById(R.id.btn_save)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        save.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {
        val empName = etEmpName.text.toString()
        val empAge = etEmpAge.text.toString()
        val empSalary = etEmpSalary.text.toString()

        if (empName.isEmpty()) {
            etEmpName.error = "Please enter Name"
        }
        if (empAge.isEmpty()) {
            etEmpAge.error = "Please enter Age"
        }
        if (empSalary.isEmpty()) {
            etEmpSalary.error = "Please enter Salary"
        }

        val empId = dbRef.push().key!!

        val employee = EmployeeModel(empId, empName, empAge, empSalary)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etEmpName.text.clear()
                etEmpAge.text.clear()
                etEmpSalary.text.clear()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()

            }

    }
}