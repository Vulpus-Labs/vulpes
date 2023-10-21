package com.vulpuslabs.chebysynth;


import voltage.controllers.*;
import voltage.core.*;
import voltage.core.Jack.JackType;
import voltage.sources.*;
import voltage.utility.*;
import voltage.processors.*;
import voltage.effects.*;
import java.awt.*;

//[user-imports]   Add your own imports here
import com.vulpuslabs.vulpes.modules.chebysynth.*;
import com.vulpuslabs.vulpes.values.events.*;
import javax.sound.midi.ShortMessage;
import java.util.List;

//[/user-imports]


public class Chebysynth extends VoltageModule
//[user-inheritance]
//[/user-inheritance]
{

   public Chebysynth( long moduleID, VoltageObjects voltageObjects )
   {
      super( moduleID, voltageObjects, "Chebysynth", ModuleType.ModuleType_Instruments, 4.8 );

      InitializeControls();
      InitializeControls2();


      canBeBypassed = false;
      SetSkin( "aefeccf5e3964d15b6504826f2fd9db3" );
   }

void InitializeControls()
{

      midiIn = new VoltageMidiJack( "midiIn", "MIDI In", this, JackType.JackType_MidiInput );
      AddComponent( midiIn );
      midiIn.SetWantsMouseNotifications( false );
      midiIn.SetPosition( 8, 291 );
      midiIn.SetSize( 57, 57 );
      midiIn.SetSkin( "MIDI Jack" );

      outputLeft = new VoltageAudioJack( "outputLeft", "Left Output", this, JackType.JackType_AudioOutput );
      AddComponent( outputLeft );
      outputLeft.SetWantsMouseNotifications( false );
      outputLeft.SetPosition( 256, 301 );
      outputLeft.SetSize( 37, 37 );
      outputLeft.SetSkin( "Jack Straight" );

      volumeAttackSlider = new VoltageSlider( "volumeAttackSlider", "Volume Attack", this, true, 0.0, 1.0, 0.1, 0 );
      AddComponent( volumeAttackSlider );
      volumeAttackSlider.SetWantsMouseNotifications( false );
      volumeAttackSlider.SetPosition( 25, 53 );
      volumeAttackSlider.SetSize( 18, 65 );
      volumeAttackSlider.SetSkin( "Straight Black Plain Ext Track" );
      volumeAttackSlider.DisplayValueInPercent( true );
      volumeAttackSlider.SetMidpointValue( 0.2 );

      volumeDecaySlider = new VoltageSlider( "volumeDecaySlider", "Volume Decay", this, true, 0.0, 1.0, 0.1, 0 );
      AddComponent( volumeDecaySlider );
      volumeDecaySlider.SetWantsMouseNotifications( false );
      volumeDecaySlider.SetPosition( 48, 53 );
      volumeDecaySlider.SetSize( 18, 65 );
      volumeDecaySlider.SetSkin( "Straight Black Plain Ext Track" );
      volumeDecaySlider.DisplayValueInPercent( true );
      volumeDecaySlider.SetMidpointValue( 0.2 );

      volumeSustainSlider = new VoltageSlider( "volumeSustainSlide", "Volume Sustain", this, true, 0.0, 1.0, 0.5, 0 );
      AddComponent( volumeSustainSlider );
      volumeSustainSlider.SetWantsMouseNotifications( false );
      volumeSustainSlider.SetPosition( 71, 53 );
      volumeSustainSlider.SetSize( 18, 65 );
      volumeSustainSlider.SetSkin( "Straight Black Plain Ext Track" );
      volumeSustainSlider.DisplayValueInPercent( true );

      volumeReleaseSlider = new VoltageSlider( "volumeReleaseSlider", "Volume Release", this, true, 0.0, 1.0, 0.1, 0 );
      AddComponent( volumeReleaseSlider );
      volumeReleaseSlider.SetWantsMouseNotifications( false );
      volumeReleaseSlider.SetPosition( 94, 53 );
      volumeReleaseSlider.SetSize( 18, 65 );
      volumeReleaseSlider.SetSkin( "Straight Black Plain Ext Track" );
      volumeReleaseSlider.DisplayValueInPercent( true );
      volumeReleaseSlider.SetMidpointValue( 0.1 );

      textLabel1 = new VoltageLabel( "textLabel1", "textLabel1", this, "VOLUME" );
      AddComponent( textLabel1 );
      textLabel1.SetWantsMouseNotifications( false );
      textLabel1.SetPosition( 19, 39 );
      textLabel1.SetSize( 99, 15 );
      textLabel1.SetEditable( false, false );
      textLabel1.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel1.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel1.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel1.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel1.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel1.SetBorderSize( 1 );
      textLabel1.SetMultiLineEdit( false );
      textLabel1.SetIsNumberEditor( false );
      textLabel1.SetNumberEditorRange( 0, 100 );
      textLabel1.SetNumberEditorInterval( 1 );
      textLabel1.SetNumberEditorUsesMouseWheel( false );
      textLabel1.SetHasCustomTextHoverColor( false );
      textLabel1.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel1.SetFont( "Verdana", 10, true, false );

      textLabel2 = new VoltageLabel( "textLabel2", "textLabel2", this, "CHEBYSYNTH" );
      AddComponent( textLabel2 );
      textLabel2.SetWantsMouseNotifications( false );
      textLabel2.SetPosition( 0, 0 );
      textLabel2.SetSize( 345, 15 );
      textLabel2.SetEditable( false, false );
      textLabel2.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel2.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel2.SetColor( new Color( 255, 255, 255, 255 ) );
      textLabel2.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel2.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel2.SetBorderSize( 1 );
      textLabel2.SetMultiLineEdit( false );
      textLabel2.SetIsNumberEditor( false );
      textLabel2.SetNumberEditorRange( 0, 100 );
      textLabel2.SetNumberEditorInterval( 1 );
      textLabel2.SetNumberEditorUsesMouseWheel( false );
      textLabel2.SetHasCustomTextHoverColor( false );
      textLabel2.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel2.SetFont( "Arial", 10, true, false );

      textLabel3 = new VoltageLabel( "textLabel3", "textLabel3", this, "A" );
      AddComponent( textLabel3 );
      textLabel3.SetWantsMouseNotifications( false );
      textLabel3.SetPosition( 25, 117 );
      textLabel3.SetSize( 18, 15 );
      textLabel3.SetEditable( false, false );
      textLabel3.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel3.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel3.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel3.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel3.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel3.SetBorderSize( 1 );
      textLabel3.SetMultiLineEdit( false );
      textLabel3.SetIsNumberEditor( false );
      textLabel3.SetNumberEditorRange( 0, 100 );
      textLabel3.SetNumberEditorInterval( 1 );
      textLabel3.SetNumberEditorUsesMouseWheel( false );
      textLabel3.SetHasCustomTextHoverColor( false );
      textLabel3.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel3.SetFont( "Times New Roman", 10, true, false );

      textLabel4 = new VoltageLabel( "textLabel4", "textLabel4", this, "D" );
      AddComponent( textLabel4 );
      textLabel4.SetWantsMouseNotifications( false );
      textLabel4.SetPosition( 48, 117 );
      textLabel4.SetSize( 18, 15 );
      textLabel4.SetEditable( false, false );
      textLabel4.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel4.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel4.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel4.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel4.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel4.SetBorderSize( 1 );
      textLabel4.SetMultiLineEdit( false );
      textLabel4.SetIsNumberEditor( false );
      textLabel4.SetNumberEditorRange( 0, 100 );
      textLabel4.SetNumberEditorInterval( 1 );
      textLabel4.SetNumberEditorUsesMouseWheel( false );
      textLabel4.SetHasCustomTextHoverColor( false );
      textLabel4.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel4.SetFont( "Times New Roman", 10, true, false );

      textLabel5 = new VoltageLabel( "textLabel5", "textLabel5", this, "S" );
      AddComponent( textLabel5 );
      textLabel5.SetWantsMouseNotifications( false );
      textLabel5.SetPosition( 71, 117 );
      textLabel5.SetSize( 18, 15 );
      textLabel5.SetEditable( false, false );
      textLabel5.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel5.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel5.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel5.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel5.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel5.SetBorderSize( 1 );
      textLabel5.SetMultiLineEdit( false );
      textLabel5.SetIsNumberEditor( false );
      textLabel5.SetNumberEditorRange( 0, 100 );
      textLabel5.SetNumberEditorInterval( 1 );
      textLabel5.SetNumberEditorUsesMouseWheel( false );
      textLabel5.SetHasCustomTextHoverColor( false );
      textLabel5.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel5.SetFont( "Times New Roman", 10, true, false );

      textLabel6 = new VoltageLabel( "textLabel6", "textLabel6", this, "R" );
      AddComponent( textLabel6 );
      textLabel6.SetWantsMouseNotifications( false );
      textLabel6.SetPosition( 94, 117 );
      textLabel6.SetSize( 18, 15 );
      textLabel6.SetEditable( false, false );
      textLabel6.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel6.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel6.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel6.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel6.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel6.SetBorderSize( 1 );
      textLabel6.SetMultiLineEdit( false );
      textLabel6.SetIsNumberEditor( false );
      textLabel6.SetNumberEditorRange( 0, 100 );
      textLabel6.SetNumberEditorInterval( 1 );
      textLabel6.SetNumberEditorUsesMouseWheel( false );
      textLabel6.SetHasCustomTextHoverColor( false );
      textLabel6.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel6.SetFont( "Times New Roman", 10, true, false );

      harmonicAttackSlider = new VoltageSlider( "harmonicAttackSlider", "Harmonic Attack", this, true, 0.0, 1.0, 0.1, 0 );
      AddComponent( harmonicAttackSlider );
      harmonicAttackSlider.SetWantsMouseNotifications( false );
      harmonicAttackSlider.SetPosition( 135, 53 );
      harmonicAttackSlider.SetSize( 18, 65 );
      harmonicAttackSlider.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAttackSlider.DisplayValueInPercent( true );
      harmonicAttackSlider.SetMidpointValue( 0.2 );

      harmonicDecaySlider = new VoltageSlider( "harmonicDecaySlider", "Harmonic Decay", this, true, 0.0, 1.0, 0.1, 0 );
      AddComponent( harmonicDecaySlider );
      harmonicDecaySlider.SetWantsMouseNotifications( false );
      harmonicDecaySlider.SetPosition( 158, 53 );
      harmonicDecaySlider.SetSize( 18, 65 );
      harmonicDecaySlider.SetSkin( "Straight Black Plain Ext Track" );
      harmonicDecaySlider.DisplayValueInPercent( true );
      harmonicDecaySlider.SetMidpointValue( 0.2 );

      harmonicSustainSlider = new VoltageSlider( "harmonicSustainSlider", "Harmonic Sustain", this, true, 0.0, 1.0, 0.5, 0 );
      AddComponent( harmonicSustainSlider );
      harmonicSustainSlider.SetWantsMouseNotifications( false );
      harmonicSustainSlider.SetPosition( 181, 53 );
      harmonicSustainSlider.SetSize( 18, 65 );
      harmonicSustainSlider.SetSkin( "Straight Black Plain Ext Track" );
      harmonicSustainSlider.DisplayValueInPercent( true );

      harmonicReleaseSlider = new VoltageSlider( "harmonicReleaseSlider", "Harmonic Release", this, true, 0.0, 1.0, 0.1, 0 );
      AddComponent( harmonicReleaseSlider );
      harmonicReleaseSlider.SetWantsMouseNotifications( false );
      harmonicReleaseSlider.SetPosition( 204, 53 );
      harmonicReleaseSlider.SetSize( 18, 65 );
      harmonicReleaseSlider.SetSkin( "Straight Black Plain Ext Track" );
      harmonicReleaseSlider.DisplayValueInPercent( true );
      harmonicReleaseSlider.SetMidpointValue( 0.1 );

      textLabel7 = new VoltageLabel( "textLabel7", "textLabel7", this, "HARMONIC" );
      AddComponent( textLabel7 );
      textLabel7.SetWantsMouseNotifications( false );
      textLabel7.SetPosition( 129, 39 );
      textLabel7.SetSize( 99, 15 );
      textLabel7.SetEditable( false, false );
      textLabel7.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel7.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel7.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel7.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel7.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel7.SetBorderSize( 1 );
      textLabel7.SetMultiLineEdit( false );
      textLabel7.SetIsNumberEditor( false );
      textLabel7.SetNumberEditorRange( 0, 100 );
      textLabel7.SetNumberEditorInterval( 1 );
      textLabel7.SetNumberEditorUsesMouseWheel( false );
      textLabel7.SetHasCustomTextHoverColor( false );
      textLabel7.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel7.SetFont( "Verdana", 10, true, false );

      textLabel8 = new VoltageLabel( "textLabel8", "textLabel8", this, "A" );
      AddComponent( textLabel8 );
      textLabel8.SetWantsMouseNotifications( false );
      textLabel8.SetPosition( 135, 117 );
      textLabel8.SetSize( 18, 15 );
      textLabel8.SetEditable( false, false );
      textLabel8.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel8.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel8.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel8.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel8.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel8.SetBorderSize( 1 );
      textLabel8.SetMultiLineEdit( false );
      textLabel8.SetIsNumberEditor( false );
      textLabel8.SetNumberEditorRange( 0, 100 );
      textLabel8.SetNumberEditorInterval( 1 );
      textLabel8.SetNumberEditorUsesMouseWheel( false );
      textLabel8.SetHasCustomTextHoverColor( false );
      textLabel8.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel8.SetFont( "Times New Roman", 10, true, false );

      textLabel9 = new VoltageLabel( "textLabel9", "textLabel9", this, "D" );
      AddComponent( textLabel9 );
      textLabel9.SetWantsMouseNotifications( false );
      textLabel9.SetPosition( 158, 117 );
      textLabel9.SetSize( 18, 15 );
      textLabel9.SetEditable( false, false );
      textLabel9.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel9.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel9.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel9.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel9.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel9.SetBorderSize( 1 );
      textLabel9.SetMultiLineEdit( false );
      textLabel9.SetIsNumberEditor( false );
      textLabel9.SetNumberEditorRange( 0, 100 );
      textLabel9.SetNumberEditorInterval( 1 );
      textLabel9.SetNumberEditorUsesMouseWheel( false );
      textLabel9.SetHasCustomTextHoverColor( false );
      textLabel9.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel9.SetFont( "Times New Roman", 10, true, false );

      textLabel10 = new VoltageLabel( "textLabel10", "textLabel10", this, "S" );
      AddComponent( textLabel10 );
      textLabel10.SetWantsMouseNotifications( false );
      textLabel10.SetPosition( 181, 117 );
      textLabel10.SetSize( 18, 15 );
      textLabel10.SetEditable( false, false );
      textLabel10.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel10.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel10.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel10.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel10.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel10.SetBorderSize( 1 );
      textLabel10.SetMultiLineEdit( false );
      textLabel10.SetIsNumberEditor( false );
      textLabel10.SetNumberEditorRange( 0, 100 );
      textLabel10.SetNumberEditorInterval( 1 );
      textLabel10.SetNumberEditorUsesMouseWheel( false );
      textLabel10.SetHasCustomTextHoverColor( false );
      textLabel10.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel10.SetFont( "Times New Roman", 10, true, false );

      textLabel11 = new VoltageLabel( "textLabel11", "textLabel11", this, "R" );
      AddComponent( textLabel11 );
      textLabel11.SetWantsMouseNotifications( false );
      textLabel11.SetPosition( 204, 117 );
      textLabel11.SetSize( 18, 15 );
      textLabel11.SetEditable( false, false );
      textLabel11.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel11.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel11.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel11.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel11.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel11.SetBorderSize( 1 );
      textLabel11.SetMultiLineEdit( false );
      textLabel11.SetIsNumberEditor( false );
      textLabel11.SetNumberEditorRange( 0, 100 );
      textLabel11.SetNumberEditorInterval( 1 );
      textLabel11.SetNumberEditorUsesMouseWheel( false );
      textLabel11.SetHasCustomTextHoverColor( false );
      textLabel11.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel11.SetFont( "Times New Roman", 10, true, false );

      textLabel12 = new VoltageLabel( "textLabel12", "textLabel12", this, "VULPUS LABS" );
      AddComponent( textLabel12 );
      textLabel12.SetWantsMouseNotifications( false );
      textLabel12.SetPosition( 0, 345 );
      textLabel12.SetSize( 345, 15 );
      textLabel12.SetEditable( false, false );
      textLabel12.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel12.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel12.SetColor( new Color( 255, 255, 255, 255 ) );
      textLabel12.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel12.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel12.SetBorderSize( 1 );
      textLabel12.SetMultiLineEdit( false );
      textLabel12.SetIsNumberEditor( false );
      textLabel12.SetNumberEditorRange( 0, 100 );
      textLabel12.SetNumberEditorInterval( 1 );
      textLabel12.SetNumberEditorUsesMouseWheel( false );
      textLabel12.SetHasCustomTextHoverColor( false );
      textLabel12.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel12.SetFont( "Arial", 10, true, false );

      textLabel13 = new VoltageLabel( "textLabel13", "textLabel13", this, "HARMONICS" );
      AddComponent( textLabel13 );
      textLabel13.SetWantsMouseNotifications( false );
      textLabel13.SetPosition( 5, 158 );
      textLabel13.SetSize( 237, 15 );
      textLabel13.SetEditable( false, false );
      textLabel13.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel13.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel13.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel13.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel13.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel13.SetBorderSize( 1 );
      textLabel13.SetMultiLineEdit( false );
      textLabel13.SetIsNumberEditor( false );
      textLabel13.SetNumberEditorRange( 0, 100 );
      textLabel13.SetNumberEditorInterval( 1 );
      textLabel13.SetNumberEditorUsesMouseWheel( false );
      textLabel13.SetHasCustomTextHoverColor( false );
      textLabel13.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel13.SetFont( "Verdana", 10, true, false );

      textLabel27 = new VoltageLabel( "textLabel27", "textLabel27", this, "ENVELOPES" );
      AddComponent( textLabel27 );
      textLabel27.SetWantsMouseNotifications( false );
      textLabel27.SetPosition( 5, 20 );
      textLabel27.SetSize( 237, 15 );
      textLabel27.SetEditable( false, false );
      textLabel27.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel27.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel27.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel27.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel27.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel27.SetBorderSize( 2 );
      textLabel27.SetMultiLineEdit( false );
      textLabel27.SetIsNumberEditor( false );
      textLabel27.SetNumberEditorRange( 0, 100 );
      textLabel27.SetNumberEditorInterval( 1 );
      textLabel27.SetNumberEditorUsesMouseWheel( false );
      textLabel27.SetHasCustomTextHoverColor( false );
      textLabel27.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel27.SetFont( "Verdana", 10, true, false );

      harmonicAmount0 = new VoltageSlider( "harmonicAmount0", "Harmonic Amount 0", this, true, -1, 1.0, 0.5, 0 );
      AddComponent( harmonicAmount0 );
      harmonicAmount0.SetWantsMouseNotifications( false );
      harmonicAmount0.SetPosition( 11, 174 );
      harmonicAmount0.SetSize( 18, 65 );
      harmonicAmount0.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount0.DisplayValueInPercent( true );

      textLabel14 = new VoltageLabel( "textLabel14", "textLabel14", this, "0" );
      AddComponent( textLabel14 );
      textLabel14.SetWantsMouseNotifications( false );
      textLabel14.SetPosition( 11, 266 );
      textLabel14.SetSize( 18, 15 );
      textLabel14.SetEditable( false, false );
      textLabel14.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel14.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel14.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel14.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel14.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel14.SetBorderSize( 1 );
      textLabel14.SetMultiLineEdit( false );
      textLabel14.SetIsNumberEditor( false );
      textLabel14.SetNumberEditorRange( 0, 100 );
      textLabel14.SetNumberEditorInterval( 1 );
      textLabel14.SetNumberEditorUsesMouseWheel( false );
      textLabel14.SetHasCustomTextHoverColor( false );
      textLabel14.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel14.SetFont( "Verdana", 10, true, false );

      textLabel32 = new VoltageLabel( "textLabel32", "textLabel32", this, "L" );
      AddComponent( textLabel32 );
      textLabel32.SetWantsMouseNotifications( false );
      textLabel32.SetPosition( 251, 329 );
      textLabel32.SetSize( 18, 15 );
      textLabel32.SetEditable( false, false );
      textLabel32.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel32.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel32.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel32.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel32.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel32.SetBorderSize( 1 );
      textLabel32.SetMultiLineEdit( false );
      textLabel32.SetIsNumberEditor( false );
      textLabel32.SetNumberEditorRange( 0, 100 );
      textLabel32.SetNumberEditorInterval( 1 );
      textLabel32.SetNumberEditorUsesMouseWheel( false );
      textLabel32.SetHasCustomTextHoverColor( false );
      textLabel32.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel32.SetFont( "Verdana", 10, true, false );

      textLabel33 = new VoltageLabel( "textLabel33", "textLabel33", this, "R" );
      AddComponent( textLabel33 );
      textLabel33.SetWantsMouseNotifications( false );
      textLabel33.SetPosition( 320, 329 );
      textLabel33.SetSize( 18, 15 );
      textLabel33.SetEditable( false, false );
      textLabel33.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel33.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel33.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel33.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel33.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel33.SetBorderSize( 1 );
      textLabel33.SetMultiLineEdit( false );
      textLabel33.SetIsNumberEditor( false );
      textLabel33.SetNumberEditorRange( 0, 100 );
      textLabel33.SetNumberEditorInterval( 1 );
      textLabel33.SetNumberEditorUsesMouseWheel( false );
      textLabel33.SetHasCustomTextHoverColor( false );
      textLabel33.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel33.SetFont( "Verdana", 10, true, false );

      harmonicEnvelope0 = new VoltageKnob( "harmonicEnvelope0", "Harmonic Envelope 0", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope0 );
      harmonicEnvelope0.SetWantsMouseNotifications( false );
      harmonicEnvelope0.SetPosition( 11, 242 );
      harmonicEnvelope0.SetSize( 18, 18 );
      harmonicEnvelope0.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope0.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope0.SetKnobParams( 215, 145 );
      harmonicEnvelope0.DisplayValueInPercent( true );
      harmonicEnvelope0.SetKnobAdjustsRing( true );

      harmonicAmount1 = new VoltageSlider( "harmonicAmount1", "Harmonic Amount 1", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount1 );
      harmonicAmount1.SetWantsMouseNotifications( false );
      harmonicAmount1.SetPosition( 34, 174 );
      harmonicAmount1.SetSize( 18, 65 );
      harmonicAmount1.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount1.DisplayValueInPercent( true );

      textLabel15 = new VoltageLabel( "textLabel15", "textLabel15", this, "1" );
      AddComponent( textLabel15 );
      textLabel15.SetWantsMouseNotifications( false );
      textLabel15.SetPosition( 34, 266 );
      textLabel15.SetSize( 18, 15 );
      textLabel15.SetEditable( false, false );
      textLabel15.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel15.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel15.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel15.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel15.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel15.SetBorderSize( 1 );
      textLabel15.SetMultiLineEdit( false );
      textLabel15.SetIsNumberEditor( false );
      textLabel15.SetNumberEditorRange( 0, 100 );
      textLabel15.SetNumberEditorInterval( 1 );
      textLabel15.SetNumberEditorUsesMouseWheel( false );
      textLabel15.SetHasCustomTextHoverColor( false );
      textLabel15.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel15.SetFont( "Verdana", 10, true, false );

      harmonicEnvelope1 = new VoltageKnob( "harmonicEnvelope1", "Harmonic Envelope 1", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope1 );
      harmonicEnvelope1.SetWantsMouseNotifications( false );
      harmonicEnvelope1.SetPosition( 34, 242 );
      harmonicEnvelope1.SetSize( 18, 18 );
      harmonicEnvelope1.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope1.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope1.SetKnobParams( 215, 145 );
      harmonicEnvelope1.DisplayValueInPercent( true );
      harmonicEnvelope1.SetKnobAdjustsRing( true );

      harmonicAmount2 = new VoltageSlider( "harmonicAmount2", "Harmonic Amount 2", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount2 );
      harmonicAmount2.SetWantsMouseNotifications( false );
      harmonicAmount2.SetPosition( 57, 174 );
      harmonicAmount2.SetSize( 18, 65 );
      harmonicAmount2.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount2.DisplayValueInPercent( true );

      textLabel16 = new VoltageLabel( "textLabel16", "textLabel16", this, "2" );
      AddComponent( textLabel16 );
      textLabel16.SetWantsMouseNotifications( false );
      textLabel16.SetPosition( 57, 266 );
      textLabel16.SetSize( 18, 15 );
      textLabel16.SetEditable( false, false );
      textLabel16.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel16.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel16.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel16.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel16.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel16.SetBorderSize( 1 );
      textLabel16.SetMultiLineEdit( false );
      textLabel16.SetIsNumberEditor( false );
      textLabel16.SetNumberEditorRange( 0, 100 );
      textLabel16.SetNumberEditorInterval( 1 );
      textLabel16.SetNumberEditorUsesMouseWheel( false );
      textLabel16.SetHasCustomTextHoverColor( false );
      textLabel16.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel16.SetFont( "Verdana", 10, true, false );

      harmonicEnvelope2 = new VoltageKnob( "harmonicEnvelope2", "Harmonic Envelope 2", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope2 );
      harmonicEnvelope2.SetWantsMouseNotifications( false );
      harmonicEnvelope2.SetPosition( 57, 242 );
      harmonicEnvelope2.SetSize( 18, 18 );
      harmonicEnvelope2.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope2.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope2.SetKnobParams( 215, 145 );
      harmonicEnvelope2.DisplayValueInPercent( true );
      harmonicEnvelope2.SetKnobAdjustsRing( true );

      harmonicAmount3 = new VoltageSlider( "harmonicAmount3", "Harmonic Amount 3", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount3 );
      harmonicAmount3.SetWantsMouseNotifications( false );
      harmonicAmount3.SetPosition( 80, 174 );
      harmonicAmount3.SetSize( 18, 65 );
      harmonicAmount3.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount3.DisplayValueInPercent( true );

      harmonicAmount4 = new VoltageSlider( "harmonicAmount4", "Harmonic Amount 4", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount4 );
      harmonicAmount4.SetWantsMouseNotifications( false );
      harmonicAmount4.SetPosition( 103, 174 );
      harmonicAmount4.SetSize( 18, 65 );
      harmonicAmount4.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount4.DisplayValueInPercent( true );

      harmonicAmount5 = new VoltageSlider( "harmonicAmount5", "Harmonic Amount 5", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount5 );
      harmonicAmount5.SetWantsMouseNotifications( false );
      harmonicAmount5.SetPosition( 126, 174 );
      harmonicAmount5.SetSize( 18, 65 );
      harmonicAmount5.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount5.DisplayValueInPercent( true );

      textLabel17 = new VoltageLabel( "textLabel17", "textLabel17", this, "3" );
      AddComponent( textLabel17 );
      textLabel17.SetWantsMouseNotifications( false );
      textLabel17.SetPosition( 80, 266 );
      textLabel17.SetSize( 18, 15 );
      textLabel17.SetEditable( false, false );
      textLabel17.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel17.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel17.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel17.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel17.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel17.SetBorderSize( 1 );
      textLabel17.SetMultiLineEdit( false );
      textLabel17.SetIsNumberEditor( false );
      textLabel17.SetNumberEditorRange( 0, 100 );
      textLabel17.SetNumberEditorInterval( 1 );
      textLabel17.SetNumberEditorUsesMouseWheel( false );
      textLabel17.SetHasCustomTextHoverColor( false );
      textLabel17.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel17.SetFont( "Verdana", 10, true, false );

      textLabel18 = new VoltageLabel( "textLabel18", "textLabel18", this, "4" );
      AddComponent( textLabel18 );
      textLabel18.SetWantsMouseNotifications( false );
      textLabel18.SetPosition( 103, 266 );
      textLabel18.SetSize( 18, 15 );
      textLabel18.SetEditable( false, false );
      textLabel18.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel18.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel18.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel18.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel18.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel18.SetBorderSize( 1 );
      textLabel18.SetMultiLineEdit( false );
      textLabel18.SetIsNumberEditor( false );
      textLabel18.SetNumberEditorRange( 0, 100 );
      textLabel18.SetNumberEditorInterval( 1 );
      textLabel18.SetNumberEditorUsesMouseWheel( false );
      textLabel18.SetHasCustomTextHoverColor( false );
      textLabel18.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel18.SetFont( "Verdana", 10, true, false );

      textLabel19 = new VoltageLabel( "textLabel19", "textLabel19", this, "5" );
      AddComponent( textLabel19 );
      textLabel19.SetWantsMouseNotifications( false );
      textLabel19.SetPosition( 126, 266 );
      textLabel19.SetSize( 18, 15 );
      textLabel19.SetEditable( false, false );
      textLabel19.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel19.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel19.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel19.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel19.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel19.SetBorderSize( 1 );
      textLabel19.SetMultiLineEdit( false );
      textLabel19.SetIsNumberEditor( false );
      textLabel19.SetNumberEditorRange( 0, 100 );
      textLabel19.SetNumberEditorInterval( 1 );
      textLabel19.SetNumberEditorUsesMouseWheel( false );
      textLabel19.SetHasCustomTextHoverColor( false );
      textLabel19.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel19.SetFont( "Verdana", 10, true, false );

      harmonicEnvelope3 = new VoltageKnob( "harmonicEnvelope3", "Harmonic Envelope 3", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope3 );
      harmonicEnvelope3.SetWantsMouseNotifications( false );
      harmonicEnvelope3.SetPosition( 80, 242 );
      harmonicEnvelope3.SetSize( 18, 18 );
      harmonicEnvelope3.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope3.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope3.SetKnobParams( 215, 145 );
      harmonicEnvelope3.DisplayValueInPercent( true );
      harmonicEnvelope3.SetKnobAdjustsRing( true );

      harmonicEnvelope4 = new VoltageKnob( "harmonicEnvelope4", "Harmonic Envelope 4", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope4 );
      harmonicEnvelope4.SetWantsMouseNotifications( false );
      harmonicEnvelope4.SetPosition( 103, 242 );
      harmonicEnvelope4.SetSize( 18, 18 );
      harmonicEnvelope4.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope4.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope4.SetKnobParams( 215, 145 );
      harmonicEnvelope4.DisplayValueInPercent( true );
      harmonicEnvelope4.SetKnobAdjustsRing( true );

      harmonicEnvelope5 = new VoltageKnob( "harmonicEnvelope5", "Harmonic Envelope 5", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope5 );
      harmonicEnvelope5.SetWantsMouseNotifications( false );
      harmonicEnvelope5.SetPosition( 126, 242 );
      harmonicEnvelope5.SetSize( 18, 18 );
      harmonicEnvelope5.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope5.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope5.SetKnobParams( 215, 145 );
      harmonicEnvelope5.DisplayValueInPercent( true );
      harmonicEnvelope5.SetKnobAdjustsRing( true );

      harmonicAmount6 = new VoltageSlider( "harmonicAmount6", "Harmonic Amount 6", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount6 );
      harmonicAmount6.SetWantsMouseNotifications( false );
      harmonicAmount6.SetPosition( 149, 174 );
      harmonicAmount6.SetSize( 18, 65 );
      harmonicAmount6.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount6.DisplayValueInPercent( true );

      textLabel20 = new VoltageLabel( "textLabel20", "textLabel20", this, "6" );
      AddComponent( textLabel20 );
      textLabel20.SetWantsMouseNotifications( false );
      textLabel20.SetPosition( 149, 266 );
      textLabel20.SetSize( 18, 15 );
      textLabel20.SetEditable( false, false );
      textLabel20.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel20.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel20.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel20.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel20.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel20.SetBorderSize( 1 );
      textLabel20.SetMultiLineEdit( false );
      textLabel20.SetIsNumberEditor( false );
      textLabel20.SetNumberEditorRange( 0, 100 );
      textLabel20.SetNumberEditorInterval( 1 );
      textLabel20.SetNumberEditorUsesMouseWheel( false );
      textLabel20.SetHasCustomTextHoverColor( false );
      textLabel20.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel20.SetFont( "Verdana", 10, true, false );

      harmonicEnvelope6 = new VoltageKnob( "harmonicEnvelope6", "Harmonic Envelope 6", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope6 );
      harmonicEnvelope6.SetWantsMouseNotifications( false );
      harmonicEnvelope6.SetPosition( 149, 242 );
      harmonicEnvelope6.SetSize( 18, 18 );
      harmonicEnvelope6.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope6.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope6.SetKnobParams( 215, 145 );
      harmonicEnvelope6.DisplayValueInPercent( true );
      harmonicEnvelope6.SetKnobAdjustsRing( true );

      harmonicAmount7 = new VoltageSlider( "harmonicAmount7", "Harmonic Amount 7", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount7 );
      harmonicAmount7.SetWantsMouseNotifications( false );
      harmonicAmount7.SetPosition( 172, 174 );
      harmonicAmount7.SetSize( 18, 65 );
      harmonicAmount7.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount7.DisplayValueInPercent( true );

      textLabel21 = new VoltageLabel( "textLabel21", "textLabel21", this, "7" );
      AddComponent( textLabel21 );
      textLabel21.SetWantsMouseNotifications( false );
      textLabel21.SetPosition( 172, 266 );
      textLabel21.SetSize( 18, 15 );
      textLabel21.SetEditable( false, false );
      textLabel21.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel21.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel21.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel21.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel21.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel21.SetBorderSize( 1 );
      textLabel21.SetMultiLineEdit( false );
      textLabel21.SetIsNumberEditor( false );
      textLabel21.SetNumberEditorRange( 0, 100 );
      textLabel21.SetNumberEditorInterval( 1 );
      textLabel21.SetNumberEditorUsesMouseWheel( false );
      textLabel21.SetHasCustomTextHoverColor( false );
      textLabel21.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel21.SetFont( "Verdana", 10, true, false );

      harmonicEnvelope7 = new VoltageKnob( "harmonicEnvelope7", "Harmonic Envelope 7", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope7 );
      harmonicEnvelope7.SetWantsMouseNotifications( false );
      harmonicEnvelope7.SetPosition( 172, 242 );
      harmonicEnvelope7.SetSize( 18, 18 );
      harmonicEnvelope7.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope7.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope7.SetKnobParams( 215, 145 );
      harmonicEnvelope7.DisplayValueInPercent( true );
      harmonicEnvelope7.SetKnobAdjustsRing( true );

      harmonicAmount8 = new VoltageSlider( "harmonicAmount8", "Harmonic Amount 8", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount8 );
      harmonicAmount8.SetWantsMouseNotifications( false );
      harmonicAmount8.SetPosition( 195, 174 );
      harmonicAmount8.SetSize( 18, 65 );
      harmonicAmount8.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount8.DisplayValueInPercent( true );

      textLabel22 = new VoltageLabel( "textLabel22", "textLabel22", this, "8" );
      AddComponent( textLabel22 );
      textLabel22.SetWantsMouseNotifications( false );
      textLabel22.SetPosition( 195, 266 );
      textLabel22.SetSize( 18, 15 );
      textLabel22.SetEditable( false, false );
      textLabel22.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel22.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel22.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel22.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel22.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel22.SetBorderSize( 1 );
      textLabel22.SetMultiLineEdit( false );
      textLabel22.SetIsNumberEditor( false );
      textLabel22.SetNumberEditorRange( 0, 100 );
      textLabel22.SetNumberEditorInterval( 1 );
      textLabel22.SetNumberEditorUsesMouseWheel( false );
      textLabel22.SetHasCustomTextHoverColor( false );
      textLabel22.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel22.SetFont( "Verdana", 10, true, false );

      harmonicEnvelope8 = new VoltageKnob( "harmonicEnvelope8", "Harmonic Envelope 8", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope8 );
      harmonicEnvelope8.SetWantsMouseNotifications( false );
      harmonicEnvelope8.SetPosition( 195, 242 );
      harmonicEnvelope8.SetSize( 18, 18 );
      harmonicEnvelope8.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope8.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope8.SetKnobParams( 215, 145 );
      harmonicEnvelope8.DisplayValueInPercent( true );
      harmonicEnvelope8.SetKnobAdjustsRing( true );

      harmonicAmount9 = new VoltageSlider( "harmonicAmount9", "Harmonic Amount 9", this, true, -1, 1.0, 0, 0 );
      AddComponent( harmonicAmount9 );
      harmonicAmount9.SetWantsMouseNotifications( false );
      harmonicAmount9.SetPosition( 218, 174 );
      harmonicAmount9.SetSize( 18, 65 );
      harmonicAmount9.SetSkin( "Straight Black Plain Ext Track" );
      harmonicAmount9.DisplayValueInPercent( true );

      textLabel23 = new VoltageLabel( "textLabel23", "textLabel23", this, "9" );
      AddComponent( textLabel23 );
      textLabel23.SetWantsMouseNotifications( false );
      textLabel23.SetPosition( 218, 266 );
      textLabel23.SetSize( 18, 15 );
      textLabel23.SetEditable( false, false );
      textLabel23.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel23.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel23.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel23.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel23.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel23.SetBorderSize( 1 );
      textLabel23.SetMultiLineEdit( false );
      textLabel23.SetIsNumberEditor( false );
      textLabel23.SetNumberEditorRange( 0, 100 );
      textLabel23.SetNumberEditorInterval( 1 );
      textLabel23.SetNumberEditorUsesMouseWheel( false );
      textLabel23.SetHasCustomTextHoverColor( false );
      textLabel23.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel23.SetFont( "Verdana", 10, true, false );

      harmonicEnvelope9 = new VoltageKnob( "harmonicEnvelope9", "Harmonic Envelope 9", this, -1, 1.0, 0 );
      AddComponent( harmonicEnvelope9 );
      harmonicEnvelope9.SetWantsMouseNotifications( false );
      harmonicEnvelope9.SetPosition( 218, 242 );
      harmonicEnvelope9.SetSize( 18, 18 );
      harmonicEnvelope9.SetSkin( "ARP2500 Sm Violet" );
      harmonicEnvelope9.SetRange( -1, 1.0, 0, false, 0 );
      harmonicEnvelope9.SetKnobParams( 215, 145 );
      harmonicEnvelope9.DisplayValueInPercent( true );
      harmonicEnvelope9.SetKnobAdjustsRing( true );

      voicesKnob = new VoltageKnob( "voicesKnob", "Voices", this, 1, 8, 1 );
      AddComponent( voicesKnob );
      voicesKnob.SetWantsMouseNotifications( false );
      voicesKnob.SetPosition( 279, 40 );
      voicesKnob.SetSize( 30, 30 );
      voicesKnob.SetSkin( "Pointer Knob Black" );
      voicesKnob.SetRange( 1, 8, 1, false, 8 );
      voicesKnob.SetKnobParams( 215, 145 );
      voicesKnob.DisplayValueInPercent( false );
      voicesKnob.SetKnobAdjustsRing( true );

      textLabel25 = new VoltageLabel( "textLabel25", "textLabel25", this, "VOICES" );
      AddComponent( textLabel25 );
      textLabel25.SetWantsMouseNotifications( false );
      textLabel25.SetPosition( 250, 20 );
      textLabel25.SetSize( 89, 15 );
      textLabel25.SetEditable( false, false );
      textLabel25.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel25.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel25.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel25.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel25.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel25.SetBorderSize( 1 );
      textLabel25.SetMultiLineEdit( false );
      textLabel25.SetIsNumberEditor( false );
      textLabel25.SetNumberEditorRange( 0, 100 );
      textLabel25.SetNumberEditorInterval( 1 );
      textLabel25.SetNumberEditorUsesMouseWheel( false );
      textLabel25.SetHasCustomTextHoverColor( false );
      textLabel25.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel25.SetFont( "Verdana", 10, true, false );

      textLabel30 = new VoltageLabel( "textLabel30", "textLabel30", this, "MODULATION" );
      AddComponent( textLabel30 );
      textLabel30.SetWantsMouseNotifications( false );
      textLabel30.SetPosition( 250, 158 );
      textLabel30.SetSize( 89, 15 );
      textLabel30.SetEditable( false, false );
      textLabel30.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel30.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel30.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel30.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel30.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel30.SetBorderSize( 1 );
      textLabel30.SetMultiLineEdit( false );
      textLabel30.SetIsNumberEditor( false );
      textLabel30.SetNumberEditorRange( 0, 100 );
      textLabel30.SetNumberEditorInterval( 1 );
      textLabel30.SetNumberEditorUsesMouseWheel( false );
      textLabel30.SetHasCustomTextHoverColor( false );
      textLabel30.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel30.SetFont( "Verdana", 10, true, false );

      textLabel26 = new VoltageLabel( "textLabel26", "textLabel26", this, "PITCH SPREAD" );
      AddComponent( textLabel26 );
      textLabel26.SetWantsMouseNotifications( false );
      textLabel26.SetPosition( 250, 123 );
      textLabel26.SetSize( 44, 24 );
      textLabel26.SetEditable( false, false );
      textLabel26.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel26.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel26.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel26.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel26.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel26.SetBorderSize( 1 );
      textLabel26.SetMultiLineEdit( true );
      textLabel26.SetIsNumberEditor( false );
      textLabel26.SetNumberEditorRange( 0, 100 );
      textLabel26.SetNumberEditorInterval( 1 );
      textLabel26.SetNumberEditorUsesMouseWheel( false );
      textLabel26.SetHasCustomTextHoverColor( false );
      textLabel26.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel26.SetFont( "Verdana", 10, true, false );

      textLabel28 = new VoltageLabel( "textLabel28", "textLabel28", this, "COUNT" );
      AddComponent( textLabel28 );
      textLabel28.SetWantsMouseNotifications( false );
      textLabel28.SetPosition( 271, 74 );
      textLabel28.SetSize( 44, 15 );
      textLabel28.SetEditable( false, false );
      textLabel28.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel28.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel28.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel28.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel28.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel28.SetBorderSize( 1 );
      textLabel28.SetMultiLineEdit( false );
      textLabel28.SetIsNumberEditor( false );
      textLabel28.SetNumberEditorRange( 0, 100 );
      textLabel28.SetNumberEditorInterval( 1 );
      textLabel28.SetNumberEditorUsesMouseWheel( false );
      textLabel28.SetHasCustomTextHoverColor( false );
      textLabel28.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel28.SetFont( "Verdana", 10, true, false );

      textLabel29 = new VoltageLabel( "textLabel29", "textLabel29", this, "STEREO SPREAD" );
      AddComponent( textLabel29 );
      textLabel29.SetWantsMouseNotifications( false );
      textLabel29.SetPosition( 295, 123 );
      textLabel29.SetSize( 44, 24 );
      textLabel29.SetEditable( false, false );
      textLabel29.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel29.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel29.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel29.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel29.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel29.SetBorderSize( 1 );
      textLabel29.SetMultiLineEdit( true );
      textLabel29.SetIsNumberEditor( false );
      textLabel29.SetNumberEditorRange( 0, 100 );
      textLabel29.SetNumberEditorInterval( 1 );
      textLabel29.SetNumberEditorUsesMouseWheel( false );
      textLabel29.SetHasCustomTextHoverColor( false );
      textLabel29.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel29.SetFont( "Verdana", 10, true, false );

      speedLabel = new VoltageLabel( "speedLabel", "", this, "SPEED" );
      AddComponent( speedLabel );
      speedLabel.SetWantsMouseNotifications( false );
      speedLabel.SetPosition( 252, 214 );
      speedLabel.SetSize( 44, 15 );
      speedLabel.SetEditable( false, false );
      speedLabel.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      speedLabel.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      speedLabel.SetColor( new Color( 255, 255, 255, 208 ) );
      speedLabel.SetBkColor( new Color( 65, 65, 65, 0 ) );
      speedLabel.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      speedLabel.SetBorderSize( 1 );
      speedLabel.SetMultiLineEdit( false );
      speedLabel.SetIsNumberEditor( false );
      speedLabel.SetNumberEditorRange( 0, 100 );
      speedLabel.SetNumberEditorInterval( 1 );
      speedLabel.SetNumberEditorUsesMouseWheel( false );
      speedLabel.SetHasCustomTextHoverColor( false );
      speedLabel.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      speedLabel.SetFont( "Verdana", 10, true, false );

      vibratoDepthLabel = new VoltageLabel( "vibratoDepthLabel", "Vibrato Depth Label", this, "DEPTH" );
      AddComponent( vibratoDepthLabel );
      vibratoDepthLabel.SetWantsMouseNotifications( false );
      vibratoDepthLabel.SetPosition( 251, 266 );
      vibratoDepthLabel.SetSize( 44, 15 );
      vibratoDepthLabel.SetEditable( false, false );
      vibratoDepthLabel.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      vibratoDepthLabel.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      vibratoDepthLabel.SetColor( new Color( 255, 255, 255, 208 ) );
      vibratoDepthLabel.SetBkColor( new Color( 65, 65, 65, 0 ) );
      vibratoDepthLabel.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      vibratoDepthLabel.SetBorderSize( 1 );
      vibratoDepthLabel.SetMultiLineEdit( false );
      vibratoDepthLabel.SetIsNumberEditor( false );
      vibratoDepthLabel.SetNumberEditorRange( 0, 100 );
      vibratoDepthLabel.SetNumberEditorInterval( 1 );
      vibratoDepthLabel.SetNumberEditorUsesMouseWheel( false );
      vibratoDepthLabel.SetHasCustomTextHoverColor( false );
      vibratoDepthLabel.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      vibratoDepthLabel.SetFont( "Verdana", 10, true, false );

      vibratoSpeedKnob = new VoltageKnob( "vibratoSpeedKnob", "Vibrato Speed", this, 0.0, 1.0, 0.5 );
      AddComponent( vibratoSpeedKnob );
      vibratoSpeedKnob.SetWantsMouseNotifications( false );
      vibratoSpeedKnob.SetPosition( 261, 184 );
      vibratoSpeedKnob.SetSize( 27, 27 );
      vibratoSpeedKnob.SetSkin( "Plastic Cream" );
      vibratoSpeedKnob.SetRange( 0.0, 1.0, 0.5, false, 0 );
      vibratoSpeedKnob.SetKnobParams( 215, 145 );
      vibratoSpeedKnob.DisplayValueInPercent( true );
      vibratoSpeedKnob.SetKnobAdjustsRing( true );

      spreadKnob = new VoltageKnob( "spreadKnob", "Spread", this, 0.0, 1.0, 0 );
      AddComponent( spreadKnob );
      spreadKnob.SetWantsMouseNotifications( false );
      spreadKnob.SetPosition( 259, 93 );
      spreadKnob.SetSize( 27, 27 );
      spreadKnob.SetSkin( "Plastic Blue" );
      spreadKnob.SetRange( 0.0, 1.0, 0, false, 0 );
      spreadKnob.SetKnobParams( 215, 145 );
      spreadKnob.DisplayValueInPercent( true );
      spreadKnob.SetKnobAdjustsRing( true );

      vibratoDepthKnob = new VoltageKnob( "vibratoDepthKnob", "Vibrato Depth", this, 0.0, 1.0, 0 );
      AddComponent( vibratoDepthKnob );
      vibratoDepthKnob.SetWantsMouseNotifications( false );
      vibratoDepthKnob.SetPosition( 260, 236 );
      vibratoDepthKnob.SetSize( 27, 27 );
      vibratoDepthKnob.SetSkin( "Plastic Plum" );
      vibratoDepthKnob.SetRange( 0.0, 1.0, 0, false, 0 );
      vibratoDepthKnob.SetKnobParams( 215, 145 );
      vibratoDepthKnob.DisplayValueInPercent( true );
      vibratoDepthKnob.SetKnobAdjustsRing( true );

      outputRight = new VoltageAudioJack( "outputRight", "Right Output", this, JackType.JackType_AudioOutput );
      AddComponent( outputRight );
      outputRight.SetWantsMouseNotifications( false );
      outputRight.SetPosition( 295, 301 );
      outputRight.SetSize( 37, 37 );
      outputRight.SetSkin( "Jack Straight" );

      stereoSpreadKnob = new VoltageKnob( "stereoSpreadKnob", "Stereo Spread", this, 0.0, 1.0, 0.5 );
      AddComponent( stereoSpreadKnob );
      stereoSpreadKnob.SetWantsMouseNotifications( false );
      stereoSpreadKnob.SetPosition( 304, 93 );
      stereoSpreadKnob.SetSize( 27, 27 );
      stereoSpreadKnob.SetSkin( "Plastic Mint" );
      stereoSpreadKnob.SetRange( 0.0, 1.0, 0.5, false, 0 );
      stereoSpreadKnob.SetKnobParams( 215, 145 );
      stereoSpreadKnob.DisplayValueInPercent( true );
      stereoSpreadKnob.SetKnobAdjustsRing( true );

      vibratoDepthLabel_1 = new VoltageLabel( "vibratoDepthLabel_1", "Vibrato Depth Label_1", this, "DRIFT" );
      AddComponent( vibratoDepthLabel_1 );
      vibratoDepthLabel_1.SetWantsMouseNotifications( false );
      vibratoDepthLabel_1.SetPosition( 294, 240 );
      vibratoDepthLabel_1.SetSize( 44, 15 );
      vibratoDepthLabel_1.SetEditable( false, false );
      vibratoDepthLabel_1.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      vibratoDepthLabel_1.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      vibratoDepthLabel_1.SetColor( new Color( 255, 255, 255, 208 ) );
      vibratoDepthLabel_1.SetBkColor( new Color( 65, 65, 65, 0 ) );
      vibratoDepthLabel_1.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      vibratoDepthLabel_1.SetBorderSize( 1 );
      vibratoDepthLabel_1.SetMultiLineEdit( false );
      vibratoDepthLabel_1.SetIsNumberEditor( false );
      vibratoDepthLabel_1.SetNumberEditorRange( 0, 100 );
      vibratoDepthLabel_1.SetNumberEditorInterval( 1 );
      vibratoDepthLabel_1.SetNumberEditorUsesMouseWheel( false );
      vibratoDepthLabel_1.SetHasCustomTextHoverColor( false );
      vibratoDepthLabel_1.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      vibratoDepthLabel_1.SetFont( "Verdana", 10, true, false );

      driftKnob = new VoltageKnob( "driftKnob", "Drift", this, 0.0, 1.0, 0 );
      AddComponent( driftKnob );
      driftKnob.SetWantsMouseNotifications( false );
      driftKnob.SetPosition( 303, 210 );
      driftKnob.SetSize( 27, 27 );
      driftKnob.SetSkin( "Plastic Dark Blue" );
      driftKnob.SetRange( 0.0, 1.0, 0, false, 0 );
      driftKnob.SetKnobParams( 215, 145 );
      driftKnob.DisplayValueInPercent( true );
      driftKnob.SetKnobAdjustsRing( true );

      volumeKnob = new VoltageKnob( "volumeKnob", "Volume", this, 0.0, 2.0, 1.0 );
      AddComponent( volumeKnob );
      volumeKnob.SetWantsMouseNotifications( false );
      volumeKnob.SetPosition( 95, 307 );
      volumeKnob.SetSize( 27, 27 );
      volumeKnob.SetSkin( "Plastic Orange" );
      volumeKnob.SetRange( 0.0, 2.0, 1.0, false, 0 );
      volumeKnob.SetKnobParams( 215, 145 );
      volumeKnob.DisplayValueInPercent( true );
      volumeKnob.SetKnobAdjustsRing( true );

      feedbackKnob = new VoltageKnob( "feedbackKnob", "Feedback", this, 0.0, 1, 0.0 );
      AddComponent( feedbackKnob );
      feedbackKnob.SetWantsMouseNotifications( false );
      feedbackKnob.SetPosition( 135, 295 );
      feedbackKnob.SetSize( 27, 27 );
      feedbackKnob.SetSkin( "Plastic Maroon" );
      feedbackKnob.SetRange( 0.0, 1, 0.0, false, 0 );
      feedbackKnob.SetKnobParams( 215, 145 );
      feedbackKnob.DisplayValueInPercent( true );
      feedbackKnob.SetKnobAdjustsRing( true );
      feedbackKnob.SetMidpointValue( 0.25 );

      midiIndicator = new VoltageLED( "midiIndicator", "MIDI Indicator", this );
      AddComponent( midiIndicator );
      midiIndicator.SetWantsMouseNotifications( false );
      midiIndicator.SetPosition( 65, 293 );
      midiIndicator.SetSize( 11, 11 );
      midiIndicator.SetSkin( "Red" );

      textLabel31 = new VoltageLabel( "textLabel31", "textLabel31", this, "AUDIO OUT" );
      AddComponent( textLabel31 );
      textLabel31.SetWantsMouseNotifications( false );
      textLabel31.SetPosition( 253, 287 );
      textLabel31.SetSize( 83, 15 );
      textLabel31.SetEditable( false, false );
      textLabel31.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel31.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel31.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel31.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel31.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel31.SetBorderSize( 1 );
      textLabel31.SetMultiLineEdit( false );
      textLabel31.SetIsNumberEditor( false );
      textLabel31.SetNumberEditorRange( 0, 100 );
      textLabel31.SetNumberEditorInterval( 1 );
      textLabel31.SetNumberEditorUsesMouseWheel( false );
      textLabel31.SetHasCustomTextHoverColor( false );
      textLabel31.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel31.SetFont( "Verdana", 10, true, false );
}

void InitializeControls2()
{

      textLabel24 = new VoltageLabel( "textLabel24", "textLabel24", this, "MIDI IN" );
      AddComponent( textLabel24 );
      textLabel24.SetWantsMouseNotifications( false );
      textLabel24.SetPosition( 52, 332 );
      textLabel24.SetSize( 60, 15 );
      textLabel24.SetEditable( false, false );
      textLabel24.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel24.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel24.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel24.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel24.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel24.SetBorderSize( 1 );
      textLabel24.SetMultiLineEdit( false );
      textLabel24.SetIsNumberEditor( false );
      textLabel24.SetNumberEditorRange( 0, 100 );
      textLabel24.SetNumberEditorInterval( 1 );
      textLabel24.SetNumberEditorUsesMouseWheel( false );
      textLabel24.SetHasCustomTextHoverColor( false );
      textLabel24.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel24.SetFont( "Verdana", 10, true, false );

      textLabel35 = new VoltageLabel( "textLabel35", "textLabel35", this, "MOD WHEEL" );
      AddComponent( textLabel35 );
      textLabel35.SetWantsMouseNotifications( false );
      textLabel35.SetPosition( 175, 324 );
      textLabel35.SetSize( 60, 15 );
      textLabel35.SetEditable( false, false );
      textLabel35.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel35.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel35.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel35.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel35.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel35.SetBorderSize( 1 );
      textLabel35.SetMultiLineEdit( false );
      textLabel35.SetIsNumberEditor( false );
      textLabel35.SetNumberEditorRange( 0, 100 );
      textLabel35.SetNumberEditorInterval( 1 );
      textLabel35.SetNumberEditorUsesMouseWheel( false );
      textLabel35.SetHasCustomTextHoverColor( false );
      textLabel35.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel35.SetFont( "Verdana", 10, true, false );

      textLabel36 = new VoltageLabel( "textLabel36", "textLabel36", this, "- VIBRATO" );
      AddComponent( textLabel36 );
      textLabel36.SetWantsMouseNotifications( false );
      textLabel36.SetPosition( 189, 287 );
      textLabel36.SetSize( 60, 15 );
      textLabel36.SetEditable( false, false );
      textLabel36.SetJustificationFlags( VoltageLabel.Justification.Left );
      textLabel36.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel36.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel36.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel36.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel36.SetBorderSize( 1 );
      textLabel36.SetMultiLineEdit( false );
      textLabel36.SetIsNumberEditor( false );
      textLabel36.SetNumberEditorRange( 0, 100 );
      textLabel36.SetNumberEditorInterval( 1 );
      textLabel36.SetNumberEditorUsesMouseWheel( false );
      textLabel36.SetHasCustomTextHoverColor( false );
      textLabel36.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel36.SetFont( "Verdana", 10, true, true );

      textLabel37 = new VoltageLabel( "textLabel37", "textLabel37", this, "- HARMONICS" );
      AddComponent( textLabel37 );
      textLabel37.SetWantsMouseNotifications( false );
      textLabel37.SetPosition( 189, 299 );
      textLabel37.SetSize( 71, 15 );
      textLabel37.SetEditable( false, false );
      textLabel37.SetJustificationFlags( VoltageLabel.Justification.Left );
      textLabel37.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel37.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel37.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel37.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel37.SetBorderSize( 1 );
      textLabel37.SetMultiLineEdit( false );
      textLabel37.SetIsNumberEditor( false );
      textLabel37.SetNumberEditorRange( 0, 100 );
      textLabel37.SetNumberEditorInterval( 1 );
      textLabel37.SetNumberEditorUsesMouseWheel( false );
      textLabel37.SetHasCustomTextHoverColor( false );
      textLabel37.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel37.SetFont( "Verdana", 10, true, true );

      textLabel38 = new VoltageLabel( "textLabel38", "textLabel38", this, "- FEEDBACK" );
      AddComponent( textLabel38 );
      textLabel38.SetWantsMouseNotifications( false );
      textLabel38.SetPosition( 189, 311 );
      textLabel38.SetSize( 60, 15 );
      textLabel38.SetEditable( false, false );
      textLabel38.SetJustificationFlags( VoltageLabel.Justification.Left );
      textLabel38.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel38.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel38.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel38.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel38.SetBorderSize( 1 );
      textLabel38.SetMultiLineEdit( false );
      textLabel38.SetIsNumberEditor( false );
      textLabel38.SetNumberEditorRange( 0, 100 );
      textLabel38.SetNumberEditorInterval( 1 );
      textLabel38.SetNumberEditorUsesMouseWheel( false );
      textLabel38.SetHasCustomTextHoverColor( false );
      textLabel38.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel38.SetFont( "Verdana", 10, true, true );

      textLabel34 = new VoltageLabel( "textLabel34", "textLabel34", this, "VOLUME" );
      AddComponent( textLabel34 );
      textLabel34.SetWantsMouseNotifications( false );
      textLabel34.SetPosition( 85, 289 );
      textLabel34.SetSize( 45, 15 );
      textLabel34.SetEditable( false, false );
      textLabel34.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel34.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel34.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel34.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel34.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel34.SetBorderSize( 1 );
      textLabel34.SetMultiLineEdit( false );
      textLabel34.SetIsNumberEditor( false );
      textLabel34.SetNumberEditorRange( 0, 100 );
      textLabel34.SetNumberEditorInterval( 1 );
      textLabel34.SetNumberEditorUsesMouseWheel( false );
      textLabel34.SetHasCustomTextHoverColor( false );
      textLabel34.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel34.SetFont( "Verdana", 10, true, false );

      textLabel39 = new VoltageLabel( "textLabel39", "textLabel39", this, "FEEDBACK" );
      AddComponent( textLabel39 );
      textLabel39.SetWantsMouseNotifications( false );
      textLabel39.SetPosition( 122, 324 );
      textLabel39.SetSize( 51, 15 );
      textLabel39.SetEditable( false, false );
      textLabel39.SetJustificationFlags( VoltageLabel.Justification.HorizCentered );
      textLabel39.SetJustificationFlags( VoltageLabel.Justification.VertCentered );
      textLabel39.SetColor( new Color( 255, 255, 255, 208 ) );
      textLabel39.SetBkColor( new Color( 65, 65, 65, 0 ) );
      textLabel39.SetBorderColor( new Color( 0, 0, 0, 0 ) );
      textLabel39.SetBorderSize( 1 );
      textLabel39.SetMultiLineEdit( false );
      textLabel39.SetIsNumberEditor( false );
      textLabel39.SetNumberEditorRange( 0, 100 );
      textLabel39.SetNumberEditorInterval( 1 );
      textLabel39.SetNumberEditorUsesMouseWheel( false );
      textLabel39.SetHasCustomTextHoverColor( false );
      textLabel39.SetTextHoverColor( new Color( 0, 0, 0, 255 ) );
      textLabel39.SetFont( "Verdana", 10, true, false );

      modWheelSelectSwitch = new VoltageSwitch( "modWheelSelectSwitch", "Mod Wheel Select", this, 2 );
      AddComponent( modWheelSelectSwitch );
      modWheelSelectSwitch.SetWantsMouseNotifications( false );
      modWheelSelectSwitch.SetPosition( 164, 290 );
      modWheelSelectSwitch.SetSize( 35, 35 );
      modWheelSelectSwitch.SetSkin( "3-State Bar Red" );
}



   //-------------------------------------------------------------------------------
   //  public void Initialize()

      //  Initialize will get called shortly after your module's constructor runs. You can use it to
      //  do any initialization that the auto-generated code doesn't handle.
   //-------------------------------------------------------------------------------
   @Override
   public void Initialize()
   {
      //[user-Initialize]   Add your own initialization code here
      volumeAdsrControls = connector.envelopeControls(
         volumeAttackSlider,
         volumeDecaySlider,
         volumeSustainSlider,
         volumeReleaseSlider);
         
      harmonicAdsrControls = connector.envelopeControls(
         harmonicAttackSlider,
         harmonicDecaySlider,
         harmonicSustainSlider,
         harmonicReleaseSlider);
         
      HarmonicWeights harmonicWeights = connector.harmonicWeights(
         new VoltageComponent[] {
            harmonicAmount0, harmonicAmount1, harmonicAmount2, harmonicAmount3,
            harmonicAmount4, harmonicAmount5, harmonicAmount6, harmonicAmount7,
            harmonicAmount8, harmonicAmount9 },
         new VoltageKnob[] {
            harmonicEnvelope0, harmonicEnvelope1, harmonicEnvelope2, harmonicEnvelope3,
            harmonicEnvelope4, harmonicEnvelope5, harmonicEnvelope6, harmonicEnvelope7,
            harmonicEnvelope8, harmonicEnvelope9 });
            
      VoiceControls voiceControls = connector.voiceControls(
         voicesKnob,
         spreadKnob,
         stereoSpreadKnob,
         vibratoSpeedKnob,
         vibratoDepthKnob,
         driftKnob,
         volumeKnob,
         feedbackKnob,
         modWheelSelectSwitch,
         outputRight);
      
      voiceEngine = new PolyVoiceEngine(
            16,
            voiceControls,
            volumeAdsrControls,
            harmonicAdsrControls,
            harmonicWeights);
            
      midiMapper = new MidiMapper(voiceEngine, voiceControls);
      StartGuiUpdateTimer(100);
      //[/user-Initialize]
   }


   //-------------------------------------------------------------------------------
   //  public void Destroy()

      //  Destroy will get called just before your module gets deleted. You can use it to perform any
      //  cleanup that's not handled automatically by Java.
   //-------------------------------------------------------------------------------
   @Override
   public void Destroy()
   {
      super.Destroy();
      //[user-Destroy]   Add your own module-getting-deleted code here



      //[/user-Destroy]
   }


   //-------------------------------------------------------------------------------
   //  public boolean Notify( VoltageComponent component, ModuleNotifications notification, double doubleValue, long longValue, int x, int y, Object object )

      //  Notify will get called when various events occur - control values changing, timers firing, etc.
   //-------------------------------------------------------------------------------
   @Override
   public boolean Notify( VoltageComponent component, ModuleNotifications notification, double doubleValue, long longValue, int x, int y, Object object )
   {
      //[user-Notify]   Add your own notification handling code between this line and the notify-close comment
      switch( notification )
      {
         case Knob_Changed:   return handler.onKnobValueChanged(component, doubleValue);
      
         case Slider_Changed:  return handler.onKnobValueChanged(component, doubleValue);
      
         case Button_Changed:   // doubleValue is the new button/toggle button value
         {
         }
         break;
      
         case Switch_Changed: return handler.onKnobValueChanged(component, doubleValue);
      
         case Jack_Connected: return handler.onJackConnected(component);
      
         case Jack_Disconnected: return handler.onJackDisconnected(component);
      
         case GUI_Update_Timer:   // Called every 50ms (by default) if turned on
         {
            if (midiReceived) {
               midiIndicator.SetValue(1.0);
               midiReceived = false;
            } else {
               midiIndicator.SetValue(0.0);
            }
         }
         break;
      
         case Object_MouseMove:   // called when mouse is over an object that receives mouse notifications. 'object' parameter is a VoltageMouseKeyFlags object.
         {
         }
         break;
      
         case Object_MouseLeave:  // called when mouse leaves an object that receives mouse notifications. 'object' parameter is a VoltageMouseKeyFlags object.
         {
         }
         break;
      
         case Object_LeftButtonDown:   // called when user left-clicks on an object that receives mouse notifications. 'object' parameter is a VoltageMouseKeyFlags object.
         {
         }
         break;
      
         case Object_LeftButtonUp:   // called when user releases left mouse button on an object that receives mouse notifications. 'object' parameter is a VoltageMouseKeyFlags object.
         {
         }
         break;
      
         case Object_RightButtonDown:   // called when user releases right mouse button on an object that receives mouse notifications. 'object' parameter is a VoltageMouseKeyFlags object.
         {
         }
         break;
      
         case Object_RightButtonUp:   // called when user right-clicks on an object that receives mouse notifications
         {
         }
         break;
      
         case Object_LeftButtonDoubleClick: // called when user left-button double-clicks on an object that receives mouse notifications
         {
         }
         break;
      
         // Less common notifications:
      
         case Named_Timer:   // object contains a String with the name of the timer that has fired
         {
         }
         break;
      
         case Canvas_Painting:   // About to paint canvas.  object is a java.awt.Rectangle with painting boundaries
         {
         }
         break;
      
         case Canvas_Painted:   // Canvas painting is complete
         {
         }
         break;
      
         case Control_DragStart:    // A user has started dragging on a control that has been marked as draggable
         {
         }
         break;
      
         case Control_DragOn:       // This control has been dragged over during a drag operation. object contains the dragged object
         {
         }
         break;
      
         case Control_DragOff:      // This control has been dragged over during a drag operation. object contains the dragged object
         {
         }
         break;
      
         case Control_DragEnd:      // A user has ended their drag on a control that has been marked as draggable
         {
         }
         break;
      
         case Label_Changed:        // The text of an editable text control has changed
         {
         }
         break;
      
         case SoundPlayback_Start:   // A sound has begun playback
         {
         }
         break;
      
         case SoundPlayback_End:     // A sound has ended playback
         {
         }
         break;
      
         case Scrollbar_Position:    // longValue is the new scrollbar position
         {
         }
         break;
      
         case PolyVoices_Changed:    // longValue is the new number of poly voices
         {
         }
         break;
      
         case File_Dropped:     // 'object' is a String containing the file path
         {
         }
         break;
      
         case Preset_Loading_Start:   // called when preset loading begins
         {
         }
         break;
      
         case Preset_Loading_Finish:  // called when preset loading finishes
         {
         }
         break;
      
         case Variation_Loading_Start:    // sent when a variation is about to load
         {
         }
         break;
      
         case Variation_Loading_Finish:   // sent when a variation has just finished loading
         {
         }
         break;
      
         case Tempo_Changed:     // doubleValue is the new tempo
         {
         }
         break;
      
         case Randomized:     // called when the module's controls get randomized
         {
         }
         break;
      
         case VariationListChanged:   // sent when a variation gets added, deleted, or renamed, or the variations list gets reordered
         {
         }
         break;
      
         case Key_Press:     // sent when module has keyboard focus and a key is pressed; object is a VoltageKeyPressInfo object
         {
         }
         break;
      
         case Reset:    // sent when the module has been reset to default settings
         {
         }
         break;
      
         case Keyboard_NoteOn:   // sent when a note has been pressed on a VoltageKeyboard object. longValue is the note value ( 0-127 )
         {
         }
         break;
      
         case Keyboard_NoteOff:   // sent when a note has been released on a VoltageKeyboard object. longValue is the note value ( 0-127 )
         {
         }
         break;
      
         case Curve_Changed:   // sent when user has edited a curve's value. 'object' will be a VoltageCurve.CurveChangeNotification object.
         {
         }
         break;
      }



      return false;
      //[/user-Notify]
   }


   //-------------------------------------------------------------------------------
   //  public void ProcessSample()

      //  ProcessSample is called once per sample. Usually it's where you read
      //  from input jacks, process audio, and write it to your output jacks.
      //  Since ProcesssSample gets called 48,000 times per second, offload CPU-intensive operations
      //  to other threads when possible and avoid calling native functions.
   //-------------------------------------------------------------------------------
   @Override
   public void ProcessSample()
   {
      //[user-ProcessSample]   Add your own process-sampling code here
      List<ShortMessage> events = midiIn.GetMessages();
      if (events != null) {
         midiReceived = true;
         for (ShortMessage event : events) {
            midiMapper.receive(event);
         }
      }
      voiceEngine.tick(outputLeft::SetValue, outputRight::SetValue);
      //[/user-ProcessSample]
   }


   //-------------------------------------------------------------------------------
   //  public String GetTooltipText( VoltageComponent component )

      //  Gets called when a tooltip is about to display for a control. Override it if
      //  you want to change what the tooltip displays - if you want a knob to work in logarithmic fashion,
      //  for instance, you can translate the knob's current value to a log-based string and display it here.
   //-------------------------------------------------------------------------------
   @Override
   public String GetTooltipText( VoltageComponent component )
   {
      //[user-GetTooltipText]   Add your own code here
      if (component == volumeAttackSlider) {
         return volumeAdsrControls.getTooltipA();
      }
      if (component == volumeDecaySlider) {
         return volumeAdsrControls.getTooltipD();
      }
      if (component == volumeReleaseSlider) {
         return volumeAdsrControls.getTooltipR();
      }
      if (component == harmonicAttackSlider) {
         return harmonicAdsrControls.getTooltipA();
      }
      if (component == harmonicDecaySlider) {
         return harmonicAdsrControls.getTooltipD();
      }
      if (component == harmonicReleaseSlider) {
         return harmonicAdsrControls.getTooltipR();
      }


      return super.GetTooltipText( component );
      //[/user-GetTooltipText]
   }


   //-------------------------------------------------------------------------------
   //  public void EditComponentValue( VoltageComponent component, double newValue, String newText )

      //  Gets called after a user clicks on a tooltip and types in a new value for a control. Override this if
      //  you've changed the default tooltip display (translating a linear value to logarithmic, for instance)
      //  in GetTooltipText().
   //-------------------------------------------------------------------------------
   @Override
   public void EditComponentValue( VoltageComponent component, double newValue, String newText )
   {
      //[user-EditComponentValue]   Add your own code here



      //[/user-EditComponentValue]
      super.EditComponentValue( component, newValue, newText );
   }


   //-------------------------------------------------------------------------------
   //  public void OnUndoRedo( String undoType, double newValue, Object optionalObject )

      //  If you've created custom undo events via calls to CreateUndoEvent, you'll need to
      //  process them in this function when they get triggered by undo/redo actions.
   //-------------------------------------------------------------------------------
   @Override
   public void OnUndoRedo( String undoType, double newValue, Object optionalObject )
   {
      //[user-OnUndoRedo]   Add your own code here



      //[/user-OnUndoRedo]
   }


   //-------------------------------------------------------------------------------
   //  public byte[] GetStateInformation()

      //  Gets called when the module's state gets saved, typically when the user saves a preset with
      //  this module in it. Voltage Modular will automatically save the states of knobs, sliders, etc.,
      //  but if you have any custom state information you need to save, return it from this function.
   //-------------------------------------------------------------------------------
   @Override
   public byte[] GetStateInformation()
   {
      //[user-GetStateInformation]   Add your own code here



      return null;
      //[/user-GetStateInformation]
   }


   //-------------------------------------------------------------------------------
   //  public void SetStateInformation(byte[] stateInfo)

      //  Gets called when this module's state is getting restored, typically when a user opens a preset with
      //  this module in it. The stateInfo parameter will contain whatever custom data you stored in GetStateInformation().
   //-------------------------------------------------------------------------------
   @Override
   public void SetStateInformation(byte[] stateInfo)
   {
      //[user-SetStateInformation]   Add your own code here



      //[/user-SetStateInformation]
   }


   //-------------------------------------------------------------------------------
   //  public byte[] GetStateInformationForVariations()

      //  Gets called when a user saves a variation with this module in it.
      //  Voltage Modular will automatically save the states of knobs, sliders, etc.,
      //  but if you have any custom state information you need to save, return it from this function.
   //-------------------------------------------------------------------------------
   @Override
   public byte[] GetStateInformationForVariations()
   {
      //[user-GetStateInformationForVariations]   Add your own code here



      return GetStateInformation();
      //[/user-GetStateInformationForVariations]
   }


   //-------------------------------------------------------------------------------
   //  public void SetStateInformationForVariations(byte[] stateInfo)

      //  Gets called when a user loads a variation with this module in it.
      //  The stateInfo parameter will contain whatever custom data you stored in GetStateInformationForVariations().
   //-------------------------------------------------------------------------------
   @Override
   public void SetStateInformationForVariations(byte[] stateInfo)
   {
      //[user-SetStateInformationForVariations]   Add your own code here
      SetStateInformation(stateInfo);



      //[/user-SetStateInformationForVariations]
   }


   // Auto-generated variables
   private VoltageSwitch modWheelSelectSwitch;
   private VoltageLabel textLabel39;
   private VoltageLabel textLabel34;
   private VoltageLabel textLabel38;
   private VoltageLabel textLabel37;
   private VoltageLabel textLabel36;
   private VoltageLabel textLabel35;
   private VoltageLabel textLabel24;
   private VoltageLabel textLabel31;
   private VoltageLED midiIndicator;
   private VoltageKnob feedbackKnob;
   private VoltageKnob volumeKnob;
   private VoltageKnob driftKnob;
   private VoltageLabel vibratoDepthLabel_1;
   private VoltageKnob stereoSpreadKnob;
   private VoltageAudioJack outputRight;
   private VoltageKnob vibratoDepthKnob;
   private VoltageKnob spreadKnob;
   private VoltageKnob vibratoSpeedKnob;
   private VoltageLabel vibratoDepthLabel;
   private VoltageLabel speedLabel;
   private VoltageLabel textLabel29;
   private VoltageLabel textLabel28;
   private VoltageLabel textLabel26;
   private VoltageLabel textLabel30;
   private VoltageLabel textLabel25;
   private VoltageKnob voicesKnob;
   private VoltageKnob harmonicEnvelope9;
   private VoltageLabel textLabel23;
   private VoltageSlider harmonicAmount9;
   private VoltageKnob harmonicEnvelope8;
   private VoltageLabel textLabel22;
   private VoltageSlider harmonicAmount8;
   private VoltageKnob harmonicEnvelope7;
   private VoltageLabel textLabel21;
   private VoltageSlider harmonicAmount7;
   private VoltageKnob harmonicEnvelope6;
   private VoltageLabel textLabel20;
   private VoltageSlider harmonicAmount6;
   private VoltageKnob harmonicEnvelope5;
   private VoltageKnob harmonicEnvelope4;
   private VoltageKnob harmonicEnvelope3;
   private VoltageLabel textLabel19;
   private VoltageLabel textLabel18;
   private VoltageLabel textLabel17;
   private VoltageSlider harmonicAmount5;
   private VoltageSlider harmonicAmount4;
   private VoltageSlider harmonicAmount3;
   private VoltageKnob harmonicEnvelope2;
   private VoltageLabel textLabel16;
   private VoltageSlider harmonicAmount2;
   private VoltageKnob harmonicEnvelope1;
   private VoltageLabel textLabel15;
   private VoltageSlider harmonicAmount1;
   private VoltageKnob harmonicEnvelope0;
   private VoltageLabel textLabel33;
   private VoltageLabel textLabel32;
   private VoltageLabel textLabel14;
   private VoltageSlider harmonicAmount0;
   private VoltageLabel textLabel27;
   private VoltageLabel textLabel13;
   private VoltageLabel textLabel12;
   private VoltageLabel textLabel11;
   private VoltageLabel textLabel10;
   private VoltageLabel textLabel9;
   private VoltageLabel textLabel8;
   private VoltageLabel textLabel7;
   private VoltageSlider harmonicReleaseSlider;
   private VoltageSlider harmonicSustainSlider;
   private VoltageSlider harmonicDecaySlider;
   private VoltageSlider harmonicAttackSlider;
   private VoltageLabel textLabel6;
   private VoltageLabel textLabel5;
   private VoltageLabel textLabel4;
   private VoltageLabel textLabel3;
   private VoltageLabel textLabel2;
   private VoltageLabel textLabel1;
   private VoltageSlider volumeReleaseSlider;
   private VoltageSlider volumeSustainSlider;
   private VoltageSlider volumeDecaySlider;
   private VoltageSlider volumeAttackSlider;
   private VoltageAudioJack outputLeft;
   private VoltageMidiJack midiIn;


   //[user-code-and-variables]    Add your own variables and functions here
private final EventBus eventBus = new EventBus();
private final Connector connector = new Connector(eventBus);
private final UIEventHandler handler = new UIEventHandler(eventBus);
private EnvelopeGeneratorControls volumeAdsrControls;
private EnvelopeGeneratorControls harmonicAdsrControls;
private PolyVoiceEngine voiceEngine;
private MidiMapper midiMapper;
private boolean midiReceived = false;


   //[/user-code-and-variables]
}

 