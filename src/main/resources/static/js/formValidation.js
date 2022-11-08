function validate() {

    if( document.getElementById('contactName').value == "" ) {
        alert( "Please provide a contact name" );
        document.newContactForm.contactName.focus() ;
        return false;
    }
    if( document.getElementById('phoneNumber').value == "" ) {
        alert( "Please provide a phone number" );
        document.newContactForm.phoneNumber.focus() ;
        return false;
    }

    if(document.getElementById('phoneNumber').value.length > 8) {
        alert( "Please provide a number with 8 digits or less" );
        document.newContactForm.phoneNumber.focus() ;
        return false;
    }

    return true;
}