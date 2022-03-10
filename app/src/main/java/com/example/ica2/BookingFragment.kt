package com.example.ica2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ica2.databinding.FragmentBookingBinding

class BookingFragment : Fragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private val channelId = "com.example.ica2"
    private var currId = 1007

    private val viewModel: AppViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The selected Location object
        val location = viewModel.locations.value?.get(arguments?.getInt("listViewPos", 0)!!)!!
        val backButton = binding.backButton
        val bookButton = binding.bookButton
        val infoTextView = binding.itemInfo

        infoTextView.text = location.description

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_Booking_to_Main)
        }

        bookButton.setOnClickListener {
            sendNotification("Booked!", "Booked location ${location.description}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


    private fun sendNotification(summary: String, message: String, group: String?, isSummary: Boolean?) {
        createNotificationChannel()
        var builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_baseline_event_available_24)
            .setContentTitle(summary)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        if (group != null && isSummary != null) {
            builder = builder.setGroup(group).setGroupSummary(isSummary)
        }
        NotificationManagerCompat.from(requireContext()).notify(currId, builder.build())
        currId++
    }

    private fun sendNotification(summary: String, message: String) {
        sendNotification(summary, message, null, null)
    }
}