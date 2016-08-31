protected void onCreate(final Bundle savedInstanceState) {
    ...
    
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Se efectuara la reserva de " + name +" el dia seleccionado.")
            .setTitle("Confirmar reserva?");

    builder.setPositiveButton(R.string.action_confirm, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            reserve(pistaId);
        }
    });
    builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
        }
    });
    final AlertDialog dialog = builder.create();

    final Button reserve = (Button)findViewById(R.id.button_reserve);
    reserve.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.show();
        }
    });
    
    ...
}