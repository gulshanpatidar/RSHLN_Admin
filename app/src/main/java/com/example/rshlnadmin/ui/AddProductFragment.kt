package com.example.rshlnadmin.ui

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.rshlnadmin.MainActivity
import com.example.rshlnadmin.R
import com.example.rshlnadmin.daos.ProductDao
import com.example.rshlnadmin.databinding.AddProductFragmentBinding
import com.example.rshlnadmin.models.Product
import com.example.rshlnadmin.ui.products.ProductsFragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class AddProductFragment : Fragment() {

    private var binding: AddProductFragmentBinding? = null
    private lateinit var viewModel: AddProductViewModel
    //these are the variables which are used to add the images
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private lateinit var productDao: ProductDao
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    companion object {
        fun newInstance() = AddProductFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddProductFragmentBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)

        binding?.viewModel = viewModel
        (activity as MainActivity).supportActionBar?.title = "Add Product"

        //initially hide the image view and show the button
        binding?.imageAddProduct?.visibility = View.GONE
        binding?.addImageButton?.visibility = View.VISIBLE
        binding?.addProductButton?.isEnabled = false

        //initialize productDao and storage reference
        productDao = ProductDao()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference.child("Product Images")

        binding?.addImageButton?.setOnClickListener{
            launchGallery()
        }
        binding?.addProductButton?.setOnClickListener {
            uploadImageToDatabase()
        }

        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val productsFragment = (activity as MainActivity).activeFragment
                val currentFragment = this@AddProductFragment
                requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).show(productsFragment).commit()
                (activity as MainActivity).supportActionBar?.title = "Products"
                val navView = (requireActivity()).findViewById<BottomNavigationView>(R.id.nav_view)
                navView.visibility = View.VISIBLE
            }
        })
    }

    //these files are for uploading the image and all
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun uploadImageToDatabase() {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("Image is uploading please wait.")
        progressBar.setTitle("Uploading...")
        progressBar.show()

        if (filePath != null) {
            val fileRef = storageReference!!.child(System.currentTimeMillis().toString())

            val uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(filePath!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

//                    postDao.addPost(binding.addPostCaption.text.toString(), url)
                    val product = Product(productName = binding?.enterProductName?.text.toString(),productImage = url,productPrice = binding?.enterProductPrice?.text.toString().toFloat(),availability = binding?.isProductAvailable?.isChecked!!,description = binding?.enterProductDescription?.text.toString())
                    productDao.addProduct(product)
                    progressBar.dismiss()
                    startFreshProductFragment()
//                    finish()
                    Toast.makeText(context, "Post Uploaded Successfully!!!", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun startFreshProductFragment(){
        val currentFragment = this@AddProductFragment
        val oldProductsFragment = (activity as MainActivity).activeFragment
        val newProductFragment = ProductsFragment()
        (activity as MainActivity).activeFragment = newProductFragment
        requireActivity().supportFragmentManager.beginTransaction().remove(currentFragment).remove(oldProductsFragment).add(R.id.nav_host_fragment_activity_main,newProductFragment,getString(R.string.products)).show(newProductFragment).commit()
        (activity as MainActivity).supportActionBar?.title = "Products"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            filePath = data.data
        }
        binding?.addImageButton?.visibility = View.GONE
        binding?.imageAddProduct?.visibility = View.VISIBLE
        binding?.imageAddProduct?.setImageURI(filePath)
        binding?.addProductButton?.isEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home){
            val productsFragment = (activity as MainActivity).activeFragment
            requireActivity().supportFragmentManager.beginTransaction().remove(this).show(productsFragment).commit()
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}