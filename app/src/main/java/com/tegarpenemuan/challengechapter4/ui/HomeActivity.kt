package com.tegarpenemuan.challengechapter4.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tegarpenemuan.challengechapter4.R
import com.tegarpenemuan.challengechapter4.adapter.NoteAdapter
import com.tegarpenemuan.challengechapter4.data.local.entity.NoteEntity
import com.tegarpenemuan.challengechapter4.database.MyDatabase
import com.tegarpenemuan.challengechapter4.databinding.ActivityHomeBinding
import com.tegarpenemuan.challengechapter4.helper.Constant
import com.tegarpenemuan.challengechapter4.model.NoteModel
import com.tegarpenemuan.challengechapter4.utils.showCustomToast
import kotlinx.coroutines.*
import java.util.*


class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    private var db: MyDatabase? = null

    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MyDatabase.getInstance(applicationContext.applicationContext)
        val authPreferences = this.getSharedPreferences(Constant.Auth.PREF_AUTH, MODE_PRIVATE)

        binding.fabPlus.setOnClickListener {
            val dialog = Dialog(this@HomeActivity)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setContentView(R.layout.dialog_add)

            val time: CharSequence = DateFormat.format("E, d MMMM yyyy HH:mm:ss", Date().getTime())
            val title = dialog.findViewById<EditText>(R.id.etTitleNote)
            val note = dialog.findViewById<EditText>(R.id.etNote)

            val btnAdd = dialog.findViewById(R.id.btnInputNote) as Button
            btnAdd.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    db?.noteDAO()?.insertNote(
                        NoteEntity(
                            id = 0,
                            date = time.toString(),
                            titleNote = title.text.toString(),
                            descNote = note.text.toString()
                        )
                    )
                }

                dialog.dismiss()
                loadDataDatabase()
                Toast(applicationContext).showCustomToast("Data Berhasil Ditambahkan", this@HomeActivity)
            }

            dialog.show()
        }

        binding.tvName.text =
            "Selamat Datang ${authPreferences.getString(Constant.Auth.KEY.USERNAME, "")}!"

        binding.btnLogout.setOnClickListener {
            authPreferences.edit().clear().commit()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        adapter = NoteAdapter(
            listener = object : NoteAdapter.EventListener {
                override fun onClick(item: NoteModel) {
                    Toast(applicationContext).showCustomToast(item.titleNote, this@HomeActivity)
                }

                override fun onDelete(item: NoteModel) {
                    val dialog = Dialog(this@HomeActivity)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.setContentView(R.layout.dialog_delete)

                    val title = dialog.findViewById(R.id.tvTitleAddDialog) as TextView
                    title.text = "Yakin Akan Menghapus Data Note ${item.titleNote} ?"

                    val btnYes = dialog.findViewById(R.id.btnIya) as Button
                    btnYes.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            db?.noteDAO()?.deleteNote(
                                NoteEntity(
                                    id = item.id,
                                    date = item.date,
                                    titleNote = item.titleNote,
                                    descNote = item.descNote
                                )
                            )
                        }

                        dialog.dismiss()
                        loadDataDatabase()
                        Toast(applicationContext).showCustomToast("Data Berhasil Dihapus", this@HomeActivity)
                    }
                    val btnNo = dialog.findViewById(R.id.btnTidak) as Button
                    btnNo.setOnClickListener {
                        dialog.dismiss()
                        loadDataDatabase()
                        Toast(applicationContext).showCustomToast("Data Batal Dihapus", this@HomeActivity)
                    }

                    dialog.show()
                }

                override fun onUpdate(item: NoteModel) {
                    val dialog = Dialog(this@HomeActivity)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.dialog_update)

                    val title = dialog.findViewById(R.id.etTitleNoteEdit) as EditText
                    val desc = dialog.findViewById(R.id.etNoteEdit) as EditText

                    title.setText(item.titleNote)
                    desc.setText(item.descNote)

                    val btnUpdate = dialog.findViewById(R.id.btnInputNote) as Button
                    btnUpdate.setOnClickListener {

                        CoroutineScope(Dispatchers.IO).launch {
                            db?.noteDAO()?.updateNote(
                                NoteEntity(
                                    id = item.id,
                                    date = item.date,
                                    titleNote = title.text.toString(),
                                    descNote = desc.text.toString()
                                )
                            )
                        }

                        dialog.dismiss()
                        loadDataDatabase()
                        Toast(applicationContext).showCustomToast("Data Berhasil Diupdate", this@HomeActivity)
                    }
                    dialog.show()
                }
            },
            notes = emptyList()
        )
    }

    override fun onStart() {
        super.onStart()
        binding.rvNote.layoutManager = LinearLayoutManager(this)
        binding.rvNote.adapter = adapter
        binding.rvNote.hasFixedSize()
        loadDataDatabase()
    }

    private fun loadDataDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val note = db?.noteDAO()?.getNotes()
            Log.d("HomeActivity", "datas: $note")
            this@HomeActivity.runOnUiThread {
                note?.let {
                    val messages = it.map {
                        NoteModel(
                            id = it.id,
                            date = it.date,
                            titleNote = it.titleNote,
                            descNote = it.descNote
                        )
                    }
                    adapter.updateList(messages)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MyDatabase.destroyInstance()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Apakah Anda ingin keluar dari aplikasi ?")
            .setCancelable(false)
            .setPositiveButton("Iya",
                DialogInterface.OnClickListener { dialog, id -> super.onBackPressed() })
            .setNegativeButton("Tidak", null)
            .show()
    }
}