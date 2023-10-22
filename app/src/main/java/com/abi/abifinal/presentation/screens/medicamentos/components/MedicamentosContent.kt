package com.abi.abifinal.presentation.screens.medicamentos.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abi.abifinal.R
import com.abi.abifinal.domain.model.Medication
import com.abi.abifinal.presentation.navigation.HomeBottomBarNavGraph
import com.abi.abifinal.presentation.screens.medicamentos.MedicamentosState
import com.abi.abifinal.presentation.screens.medicamentos.MedicamentosViewModel
import com.abi.abifinal.presentation.ui.theme.AbifinalTheme
import com.abi.abifinal.presentation.utils.getTimeRemaining
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MedicamentosContent(navController: NavHostController,
                        ) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Greeting()
        //DailyMedications(state, viewModel)
        DailyOverview()
    }
}
@Composable
fun Greeting() {
    Column {
        // TODO: Add greeting based on time of day e.g. Good Morning, Good Afternoon, Good evening.
        // TODO: Get name from DB and show user's first name.
        Text(
            text = "Good morning,",
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = "Kathryn!",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}
@Composable
//fun DailyOverview(medicationsToday: List<Medication>) {
fun DailyOverview() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        )
    ) {

        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 16.dp)
                    .fillMaxWidth(.36F),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Your plan for today",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    //text = "${medicationsToday.filter { it.medicationTaken }.size} of ${medicationsToday.size} completed",
                    text = "completed",
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doctor), contentDescription = ""
                )
            }
        }
    }
}

/*@Composable
//fun DailyMedications(state: MedicamentosState, viewModel: MedicamentosViewModel) {
fun DailyMedications() {

    val medicationList = state.medications.sortedBy { it.date }
    val combinedList: List<MedicationListItem> = mutableListOf<MedicationListItem>().apply {
        val calendar = Calendar.getInstance()
        val medicationsToday = medicationList.filter {
            val medicationDate = it.date
            calendar.time = medicationDate
            val medicationDay = calendar.get(Calendar.DAY_OF_YEAR)

            val todayCalendar = Calendar.getInstance()
            val todayDay = todayCalendar.get(Calendar.DAY_OF_YEAR)

            medicationDay == todayDay
        }

        add(MedicationListItem.DailyOverviewItem(medicationsToday))

        if (medicationsToday.isNotEmpty()) {
            add(MedicationListItem.HeaderItem("Today"))
            addAll(medicationsToday.map { MedicationListItem.MedicationItem(it) })
        }

        // Find medications for this week and add "This Week" header
        val startOfWeekThisWeek = Calendar.getInstance()
        startOfWeekThisWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val endOfWeekThisWeek = startOfWeekThisWeek.clone() as Calendar
        endOfWeekThisWeek.add(Calendar.DAY_OF_WEEK, 6)
        val medicationsThisWeek = medicationList.filter {
            val medicationDate = it.date // Change this to the appropriate attribute
            medicationDate in startOfWeekThisWeek.time..endOfWeekThisWeek.time && !medicationsToday.contains(it)
        }
        if (medicationsThisWeek.isNotEmpty()) {
            add(MedicationListItem.HeaderItem("This Week"))
            addAll(medicationsThisWeek.map { MedicationListItem.MedicationItem(it) })
        }

        // Find medications for next week and add "Next Week" header
        val startOfWeekNextWeek = Calendar.getInstance()
        startOfWeekNextWeek.time = endOfWeekThisWeek.time // Use the end of current week as start of next week
        startOfWeekNextWeek.add(Calendar.DAY_OF_MONTH, 1)
        val endOfWeekNextWeek = startOfWeekNextWeek.clone() as Calendar
        endOfWeekNextWeek.add(Calendar.DAY_OF_MONTH, 6)
        val medicationsNextWeek = medicationList.filter {
            val medicationDate = it.date // Change this to the appropriate attribute
            medicationDate in startOfWeekNextWeek.time..endOfWeekNextWeek.time
        }
        if (medicationsNextWeek.isNotEmpty()) {
            add(MedicationListItem.HeaderItem("Next Week"))
            addAll(medicationsNextWeek.map { MedicationListItem.MedicationItem(it) })
        }
    }

    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = combinedList,
            itemContent = {
                when (it) {
                    is MedicationListItem.DailyOverviewItem -> {
                        //DailyOverview(it.medicationsToday)
                    }
                    is MedicationListItem.HeaderItem -> {
                        Text(
                            modifier = Modifier
                                .padding(4.dp, 12.dp, 8.dp, 0.dp)
                                .fillMaxWidth(),
                            text = it.headerText.uppercase(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    is MedicationListItem.MedicationItem -> {
                        MedicationCard(medication = it.medication, viewModel)
                    }
                }
            }
        )
    }
}*/

sealed class MedicationListItem {
    data class DailyOverviewItem(val medicationsToday: List<Medication>) : MedicationListItem()
    data class MedicationItem(val medication: Medication) : MedicationListItem()
    data class HeaderItem(val headerText: String) : MedicationListItem()
}

@Composable
//fun MedicationCard(medication: Medication, viewModel: MedicamentosViewModel) {
fun MedicationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "helllo",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "medication"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text ="dddd" ,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {

                Button(
                    onClick = {
                        //viewModel.takeMedication(medication)
                    },
                    //enabled = !medication.medicationTaken
                ) {
                    //if (medication.medicationTaken) {
                      if(true){
                        Text(
                            text = "Taken"
                        )
                    } else {
                        Text(
                            text = "Take now"
                        )
                    }
                }
            }
        }
    }
}