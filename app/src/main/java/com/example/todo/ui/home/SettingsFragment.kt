package com.example.todo.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.databinding.FragmentSettingsBinding
import com.example.todo.utils.LocaleManager
import com.example.todo.utils.getCurrentLanguage

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setLanguageSettings()
        setModeSettings()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: get from share preferences later
        val currentCountryCode = getCurrentLanguage(requireContext())
        val language = if (currentCountryCode == "en") "English" else "Arabic"
        setLanguageDropDownMenuState(language)
        setModeDropDownMenuState()

        onModeDropDownMenuClick()
        onLanguageDropDownMenuClick()
        activity?.setTitle(getString(R.string.settings))


    }

    private fun onLanguageDropDownMenuClick() {
        binding.autoCompleteTVLanguages.setOnItemClickListener { parent, view, position, id ->
            val selectedLanguage = parent.getItemAtPosition(position).toString()
            val languageCode = if (selectedLanguage == "English") "en" else "ar"
            setLanguageDropDownMenuState(selectedLanguage)
            applyLanguageChange(languageCode)

        }
    }

    private fun onModeDropDownMenuClick() {
        binding.autoCompleteTVModes.setOnItemClickListener { parent, view, position, id ->
            val selectedMode = parent.getItemAtPosition(position).toString()
            setModeDropDownMenuState()
            val isDark = (selectedMode == "Dark")
            changeMode(isDark)

        }
    }

    private fun setModeSettings() {
        val modes = resources.getStringArray(R.array.modes)
        val arrayAdapterModes = ArrayAdapter(requireContext(), R.layout.drop_down_item, modes)
        binding.autoCompleteTVModes.setAdapter(arrayAdapterModes)
        arrayAdapterModes.notifyDataSetChanged()

    }

    private fun setLanguageSettings() {
        val languages = resources.getStringArray(R.array.languages)
        val arrayAdapterLanguages =
            ArrayAdapter(requireContext(), R.layout.drop_down_item, languages)
        binding.autoCompleteTVLanguages.setAdapter(arrayAdapterLanguages)

    }

    private fun setModeDropDownMenuState() {
        val currentNightMode =
            resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_NO) {
            binding.autoCompleteTVModes.setText(getString(R.string.light))
            changeModeDropDownMenuIcon("Light")
        } else {
            binding.autoCompleteTVModes.setText(getString(R.string.dark))
            changeModeDropDownMenuIcon("Dark")
        }

    }

    private fun setLanguageDropDownMenuState(selectedLanguage: String) {
        binding.autoCompleteTVLanguages.setText(selectedLanguage)
    }

    private fun changeModeDropDownMenuIcon(selectedMode: String) {
        if (selectedMode == "Light") {
            binding.modeTil.setStartIconDrawable(R.drawable.ic_light_mode)
        } else {
            binding.modeTil.setStartIconDrawable(R.drawable.ic_dark)
        }
        binding.modeTil.refreshStartIconDrawableState()
    }

    private fun changeMode(isDark: Boolean) {

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun applyLanguageChange(languageCode: String) {
        LocaleManager.setLocale(requireContext(), languageCode)
        recreateActivity()
    }

}

fun Fragment.recreateActivity() {
    activity?.let {
        val intent = Intent(it, it::class.java)
        it.startActivity(intent)
        it.finish()
    }
}