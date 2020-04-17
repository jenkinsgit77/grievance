

/*For Suspect List Starts*/
   function setFocusSuspectItem2(event) {
          var textfield = event.getCurrentTarget();
          var textfield_current_content = textfield.getSubmittedValue();
          var textfield_current_content_length = textfield_current_content.length;

          var sourElement = event.getSource();
          var element = sourElement.findComponent("it2");

          if (textfield_current_content_length == 4) {
              element.focus();

          }

      }

      function setFocusSuspectItem3(event) {
          var textfield = event.getCurrentTarget();
          var textfield_current_content = textfield.getSubmittedValue();
          var textfield_current_content_length = textfield_current_content.length;
          var sourElement = event.getSource();
          var element = sourElement.findComponent("it3");
          if (textfield_current_content_length == 4) {
              element.focus();
          }
      }

      function setFocusSuspectButton(event) {
          var textfield = event.getCurrentTarget();
          var textfield_current_content = textfield.getSubmittedValue();
          var textfield_current_content_length = textfield_current_content.length;

          var sourElement = event.getSource();
          var element = sourElement.findComponent("it4");
          if (textfield_current_content_length == 4) {
              element.focus();
          }
	  }
		  
		  
	/*For Suspect List Ends*/	  
		  
		  
		  
		  
		  
		  
/*For Suspect List Starts*/		  
        function clearNonMobileFindLpg(event) {
          var sourElement = event.getSource();
          var element2 = sourElement.findComponent("it6");
          var element3 = sourElement.findComponent("it8");
          element2.setValue("");
          element3.setValue("");
      }

      

      function clearMobileFindLpg(event) {
          var sourElement = event.getSource();
          var element1 = sourElement.findComponent("it3");
          element1.setValue("");
          

      }
      /*For Suspect List Ends*/	
      
      
      
    /*For OTP Starts*/	  
      
        function setFocusOTP2(event) {

          var sourElement = event.getSource();
          var element = sourElement.findComponent("it2");
          element.focus();

      }

      function setFocusOTP3(event) {

          var sourElement = event.getSource();
          var element = sourElement.findComponent("it3");
          element.focus();

      }

      function setFocusButtonOTP(event) {

          var sourElement = event.getSource();
          var element = sourElement.findComponent("it4");
          element.focus();

      }
      
      /*For OTP Ends*/	
      
      
      
        function charCount(evt) {
          var sourceId = evt.getSource().getClientId();
          var source = evt.getSource();
          var textfield = evt.getCurrentTarget();
          var textfield_current_content = textfield.getSubmittedValue();
          var textfield_current_content_length = textfield_current_content.length;
          var maxLength = textfield.getMaximumLength();
          var lengthdiff = maxLength - textfield_current_content_length;
          
          if (lengthdiff >= 0) {
              if (sourceId.indexOf("it2") !=  - 1) {
                  var counter = source.findComponent("CharCount");
              }
              counter_value = counter.getValue();
              counter.setValue("Remaining " + lengthdiff);
              showAlert = true;
          }
      }
      
      window.addEventListener('DOMContentLoaded', function () {
          $("body").on("keypress", ".numericOnlyjs input", function (e) {
              if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                  return false;
              }
          });
      });