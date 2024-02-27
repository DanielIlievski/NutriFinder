package com.example.nutrifinder.search

import app.cash.turbine.test
import com.example.nutrifinder.domain.interactors.GetFoodItemsUseCase
import com.example.nutrifinder.domain.model.FoodItem
import com.example.nutrifinder.mock.MockFoodItemsService
import com.example.nutrifinder.ui.search.SearchUiEvent
import com.example.nutrifinder.ui.search.SearchViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mock

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @MockK
    lateinit var viewModel: SearchViewModel

    @MockK
    lateinit var getFoodItemsUseCase: GetFoodItemsUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        getFoodItemsUseCase = GetFoodItemsUseCase(MockFoodItemsService())
        viewModel = SearchViewModel(getFoodItemsUseCase)
    }

    @Test
    fun `test validateQueryText valid`() = runBlocking {
        // Given
        val validQueryText = "pizza"

        // When
        viewModel.onEvent(SearchUiEvent.QueryTextChanged(validQueryText))

        // Then
        viewModel.searchFormState.test {
            val emission = awaitItem()
            assertThat(emission.isQueryTextValid).isEqualTo(true)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test validateQueryText invalid`() = runBlocking {
        // Given
        val invalidQueryText = "a"

        // When
        viewModel.onEvent(SearchUiEvent.QueryTextChanged(invalidQueryText))

        // Then
        viewModel.searchFormState.test {
            val emission = awaitItem()
            assertThat(emission.isQueryTextValid).isEqualTo(false)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test getFoodItems success`() = runBlocking {
        val validQueryText = "chicken"
        viewModel.onEvent(SearchUiEvent.QueryTextChanged(validQueryText))

        viewModel.searchFormState.test {
            skipItems(1)
            val emission = awaitItem()
            assertThat(emission.foodItems).isEqualTo(
                listOf(
                    FoodItem(0, "Journal Communications", "BBQ Chicken Pizza", 119, 231)
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test getFoodItems empty list when queryText less than 3`() = runBlocking {
        val invalidQueryText = ""
        viewModel.onEvent(SearchUiEvent.QueryTextChanged(invalidQueryText))

        viewModel.searchFormState.test {
            val emission = awaitItem()
            assertThat(emission.foodItems).isEqualTo(emptyList<FoodItem>())
            cancelAndConsumeRemainingEvents()
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}