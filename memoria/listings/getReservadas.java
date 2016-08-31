public void getReservadas (int id) {
    final String pistaId = String.valueOf(id);
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = getResources().getString(R.string.ya_reservadas_url);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayList<Long> reservadasLong = new ArrayList<Long>();
                    try {
                        JSONArray reservadas = new JSONArray(response);
                        String[] mArray = reservadas.join(",").split(",");
                        for (int i = 0; i < mArray.length; i++) {
                            reservadasLong.add(Long.parseLong(mArray[i]));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    List<CalendarDay> list = new ArrayList<CalendarDay>();
                    for (Long r : reservadasLong) {
                        CalendarDay calendarDay = CalendarDay.from(new Date(r));
                        list.add(calendarDay);
                    }
                    calendarDays = list;
                    calendar.addDecorators(new EventDecorator(R.color.colorCafe, calendarDays));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("pista_id", pistaId);
            return params;
        }
    };
    queue.add(stringRequest);
}