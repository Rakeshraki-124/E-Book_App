package com.example.e_book.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_book.ResultState
import com.example.e_book.data.repo.Repo
import com.example.e_book.data.response.BookCategoryModel
import com.example.e_book.data.response.BookModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val repo: Repo): ViewModel() {

    private val _getAllBookState = MutableStateFlow(GetAllBooksState())
    val getAllBookState = _getAllBookState.asStateFlow()

    private val _getAllBookCategoryState = MutableStateFlow(GetAllBooksCategoryState())
    val getAllBooksCategoryState = _getAllBookCategoryState.asStateFlow()

    private val _getBooksByCategoryState = MutableStateFlow(GetAllBooksByCategoryState())
    val  getBooksByCategoryState = _getBooksByCategoryState.asStateFlow()

    fun getAllBooks(){
        viewModelScope.launch(Dispatchers.IO){

            repo.getAllBooks().collect{
                Log.d("ViewModelState", "Statte: $it")
                when(it){
                    is ResultState.Loading ->
                    {
                        Log.d("ViewModelState", "Loading...")
                        _getAllBookState.value = GetAllBooksState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        Log.d("ViewModelState", "Success: ${it.data}")
                        _getAllBookState.value = GetAllBooksState(isLoading = false,data=it.data)
                    }
                    is ResultState.Error -> {
                        Log.d("ViewModelState", "Error: ${it.exception}")
                        _getAllBookState.value = GetAllBooksState(isLoading = false,error = it.exception.toString())
                    }
                }
            }
        }
      //  Log.d("OTPT", "getAllBooks: ${getAllBooks()}")
    }

    fun getAllBooksCategory(){
        viewModelScope.launch(Dispatchers.IO){

            repo.getAllBooksCategory().collect{
                when(it){
                    is ResultState.Loading ->{
                        _getAllBookCategoryState.value = GetAllBooksCategoryState(isLoading = true)
                    }
                    is ResultState.Success ->{
                        _getAllBookCategoryState.value = GetAllBooksCategoryState(isLoading = false, data = it.data)
                        Log.d("category", "getAllBooksCategory: ${it.data}")
                    }
                    is ResultState.Error -> {
                        _getAllBookCategoryState.value = GetAllBooksCategoryState(isLoading = false, error = it.exception.toString())
                    }
                }
            }
        }
    }

    fun getBooksByCategory(category : String){

        viewModelScope.launch(Dispatchers.IO){

            repo.getBookByCategory(category).collect{

                when(it){
                    is ResultState.Loading -> {
                        _getBooksByCategoryState.value = GetAllBooksByCategoryState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getBooksByCategoryState.value = GetAllBooksByCategoryState(isLoading = false, data = it.data)
                        Log.d("categoryBook","getBooksByCategory: ${it.data}")
                    }
                    is ResultState.Error -> {
                        _getBooksByCategoryState.value = GetAllBooksByCategoryState(isLoading = false, error = it.exception.toString())
                    }
                }
            }

        }
    }

}

data class GetAllBooksState(
    val isLoading: Boolean = false,
    val data: List<BookModels> = emptyList(),
    val error: String = ""
)

data class GetAllBooksCategoryState(
    val isLoading: Boolean = false,
    val data: List<BookCategoryModel> = emptyList(),
    val error: String = ""
)
data class GetAllBooksByCategoryState(
    val isLoading: Boolean = false,
    val data: List<BookModels> = emptyList(),
    val error: String = ""
)