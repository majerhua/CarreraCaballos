package carreracaballos.ia.com.carreracaballo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.stream.Collectors;
import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.Runnable.*;
import java.util.*;


public class MainActivity extends AppCompatActivity  implements  OnClickListener{

    ImageView celdaActual;

    TextView contFichaNegra;
    TextView contFichaBlanca;

    ImageView[][] casilla = new ImageView[6][11];

    //Matriz del Tablero T[][]
    Integer[][] T = new Integer[6][11];

    private int Fb;//Cantidad de Fichas Blancas
    private int Fn;//Cantidad de Fichas Negras

    //Turno
    private static int turno;
    private static boolean turnoExtra = false;

    //Coordenadas de Origen
    private static int inicioX = 0;
    private static int inicioY = 0;
    //Coordenadas de Destino
    private static int destinoX = 0;
    private static int destinoY = 0;

    //Banderas click valido
    private boolean PrimerClick = true;

    //Turno humano
    private static int turnoHumano = 0;

    private static String dificultadIA = "Principiante";

    private static Object[] FichasComputadora = new Object[3];
    private static List PosibleJugada = new ArrayList<>(); //LISTA DE POSIBLES JUGADAS QUE PUEDE REALIZAR LA COMPUTADORA
    private static Map<Object,Object> SegundoNivel = new HashMap<>();

    //HILO PARA LA JUGADA DE LA MAQUINA
    //public ExecutorService jugadaMaquina = Executors.newSingleThreadExecutor();


    public AlertDialog createRadioListDialogNivel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final CharSequence[] items = new CharSequence[3];

        items[0] = "Principiante";
        items[1] = "Intermedio";
        items[2] = "Avanzado";

        builder.setTitle("Selccione el nivel de la maquina!")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dificultadIA =  String.valueOf(items[which]);

                        Toast.makeText(
                                MainActivity.this,
                                "Seleccionaste nivel " + items[which],
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        return builder.create();
    }

    public AlertDialog createRadioListDialogFicha() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        final CharSequence[] items = new CharSequence[2];

        items[0] = "Negro";
        items[1] = "Blanco";

        builder.setTitle("Selccione color de la ficha")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        turnoHumano =  which;
                        //reateRadioListDialogNivel();
                        //createRadioListDialogFicha().hide();
                        Toast.makeText(
                                MainActivity.this,
                                "Seleccionaste ficha de color " + items[which],
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        return builder.create();
    }

    public void alertaTerminoJuagada(String mensaje) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Termino Juego!")
                .setMessage(mensaje)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //createRadioListDialogFicha().show();
        createRadioListDialogNivel().show();

        contFichaNegra = (TextView)  findViewById(R.id.contFichaNegra);
        contFichaBlanca = (TextView)  findViewById(R.id.contFichaBlanca);

        contFichaBlanca.setText("3");
        contFichaNegra.setText("3");
        /*PRIMERA FILA */
        casilla[0][0] = (ImageView) findViewById(R.id.celda0_0);
        casilla[0][0].setOnClickListener(this);

        casilla[1][0] = (ImageView) findViewById(R.id.celda1_0);
        casilla[1][0].setOnClickListener(this);

        casilla[2][0] = (ImageView) findViewById(R.id.celda2_0);
        casilla[2][0].setOnClickListener(this);

        casilla[3][0] = (ImageView) findViewById(R.id.celda3_0);
        casilla[3][0].setOnClickListener(this);

        casilla[4][0] = (ImageView) findViewById(R.id.celda4_0);
        casilla[4][0].setOnClickListener(this);

        casilla[5][0] = (ImageView) findViewById(R.id.celda5_0);
        casilla[5][0].setOnClickListener(this);

        /*SEGUNDA FILA */
        casilla[0][1] = (ImageView) findViewById(R.id.celda0_1);
        casilla[0][1].setOnClickListener(this);

        casilla[1][1] = (ImageView) findViewById(R.id.celda1_1);
        casilla[1][1].setOnClickListener(this);

        casilla[2][1] = (ImageView) findViewById(R.id.celda2_1);
        casilla[2][1].setOnClickListener(this);

        casilla[3][1] = (ImageView) findViewById(R.id.celda3_1);
        casilla[3][1].setOnClickListener(this);

        casilla[4][1] = (ImageView) findViewById(R.id.celda4_1);
        casilla[4][1].setOnClickListener(this);

        casilla[5][1] = (ImageView) findViewById(R.id.celda5_1);
        casilla[5][1].setOnClickListener(this);

        /*TERCERA FILA */
        casilla[0][2] = (ImageView) findViewById(R.id.celda0_2);
        casilla[0][2].setOnClickListener(this);

        casilla[1][2] = (ImageView) findViewById(R.id.celda1_2);
        casilla[1][2].setOnClickListener(this);

        casilla[2][2] = (ImageView) findViewById(R.id.celda2_2);
        casilla[2][2].setOnClickListener(this);

        casilla[3][2] = (ImageView) findViewById(R.id.celda3_2);
        casilla[3][2].setOnClickListener(this);

        casilla[4][2] = (ImageView) findViewById(R.id.celda4_2);
        casilla[4][2].setOnClickListener(this);

        casilla[5][2] = (ImageView) findViewById(R.id.celda5_2);
        casilla[5][2].setOnClickListener(this);

        /*CUARTA FILA */
        casilla[0][3] = (ImageView) findViewById(R.id.celda0_3);
        casilla[0][3].setOnClickListener(this);

        casilla[1][3] = (ImageView) findViewById(R.id.celda1_3);
        casilla[1][3].setOnClickListener(this);

        casilla[2][3] = (ImageView) findViewById(R.id.celda2_3);
        casilla[2][3].setOnClickListener(this);

        casilla[3][3] = (ImageView) findViewById(R.id.celda3_3);
        casilla[3][3].setOnClickListener(this);

        casilla[4][3] = (ImageView) findViewById(R.id.celda4_3);
        casilla[4][3].setOnClickListener(this);

        casilla[5][3] = (ImageView) findViewById(R.id.celda5_3);
        casilla[5][3].setOnClickListener(this);

        /*QUINTA FILA */
        casilla[0][4] = (ImageView) findViewById(R.id.celda0_4);
        casilla[0][4].setOnClickListener(this);

        casilla[1][4] = (ImageView) findViewById(R.id.celda1_4);
        casilla[1][4].setOnClickListener(this);

        casilla[2][4] = (ImageView) findViewById(R.id.celda2_4);
        casilla[2][4].setOnClickListener(this);

        casilla[3][4] = (ImageView) findViewById(R.id.celda3_4);
        casilla[3][4].setOnClickListener(this);

        casilla[4][4] = (ImageView) findViewById(R.id.celda4_4);
        casilla[4][4].setOnClickListener(this);

        casilla[5][4] = (ImageView) findViewById(R.id.celda5_4);
        casilla[5][4].setOnClickListener(this);

        /*SEXTA FILA */
        casilla[0][5] = (ImageView) findViewById(R.id.celda0_5);
        casilla[0][5].setOnClickListener(this);

        casilla[1][5] = (ImageView) findViewById(R.id.celda1_5);
        casilla[1][5].setOnClickListener(this);

        casilla[2][5] = (ImageView) findViewById(R.id.celda2_5);
        casilla[2][5].setOnClickListener(this);

        casilla[3][5] = (ImageView) findViewById(R.id.celda3_5);
        casilla[3][5].setOnClickListener(this);

        casilla[4][5] = (ImageView) findViewById(R.id.celda4_5);
        casilla[4][5].setOnClickListener(this);

        casilla[5][5] = (ImageView) findViewById(R.id.celda5_5);
        casilla[5][5].setOnClickListener(this);

        /*SEPTIMA FILA */
        casilla[0][6] = (ImageView) findViewById(R.id.celda0_6);
        casilla[0][6].setOnClickListener(this);

        casilla[1][6] = (ImageView) findViewById(R.id.celda1_6);
        casilla[1][6].setOnClickListener(this);

        casilla[2][6] = (ImageView) findViewById(R.id.celda2_6);
        casilla[2][6].setOnClickListener(this);

        casilla[3][6] = (ImageView) findViewById(R.id.celda3_6);
        casilla[3][6].setOnClickListener(this);

        casilla[4][6] = (ImageView) findViewById(R.id.celda4_6);
        casilla[4][6].setOnClickListener(this);

        casilla[5][6] = (ImageView) findViewById(R.id.celda5_6);
        casilla[5][6].setOnClickListener(this);

        /*OCTAVA FILA */
        casilla[0][7] = (ImageView) findViewById(R.id.celda0_7);
        casilla[0][7].setOnClickListener(this);

        casilla[1][7] = (ImageView) findViewById(R.id.celda1_7);
        casilla[1][7].setOnClickListener(this);

        casilla[2][7] = (ImageView) findViewById(R.id.celda2_7);
        casilla[2][7].setOnClickListener(this);

        casilla[3][7] = (ImageView) findViewById(R.id.celda3_7);
        casilla[3][7].setOnClickListener(this);

        casilla[4][7] = (ImageView) findViewById(R.id.celda4_7);
        casilla[4][7].setOnClickListener(this);

        casilla[5][7] = (ImageView) findViewById(R.id.celda5_7);
        casilla[5][7].setOnClickListener(this);

        /*NOVENA FILA */
        casilla[0][8] = (ImageView) findViewById(R.id.celda0_8);
        casilla[0][8].setOnClickListener(this);

        casilla[1][8] = (ImageView) findViewById(R.id.celda1_8);
        casilla[1][8].setOnClickListener(this);

        casilla[2][8] = (ImageView) findViewById(R.id.celda2_8);
        casilla[2][8].setOnClickListener(this);

        casilla[3][8] = (ImageView) findViewById(R.id.celda3_8);
        casilla[3][8].setOnClickListener(this);

        casilla[4][8] = (ImageView) findViewById(R.id.celda4_8);
        casilla[4][8].setOnClickListener(this);

        casilla[5][8] = (ImageView) findViewById(R.id.celda5_8);
        casilla[5][8].setOnClickListener(this);

        /*DECIMA FILA */
        casilla[0][9] = (ImageView) findViewById(R.id.celda0_9);
        casilla[0][9].setOnClickListener(this);

        casilla[1][9] = (ImageView) findViewById(R.id.celda1_9);
        casilla[1][9].setOnClickListener(this);

        casilla[2][9] = (ImageView) findViewById(R.id.celda2_9);
        casilla[2][9].setOnClickListener(this);

        casilla[3][9] = (ImageView) findViewById(R.id.celda3_9);
        casilla[3][9].setOnClickListener(this);

        casilla[4][9] = (ImageView) findViewById(R.id.celda4_9);
        casilla[4][9].setOnClickListener(this);

        casilla[5][9] = (ImageView) findViewById(R.id.celda5_9);
        casilla[5][9].setOnClickListener(this);

        /*ONCEABA FILA */
        casilla[0][10] = (ImageView) findViewById(R.id.celda0_10);
        casilla[0][10].setOnClickListener(this);

        casilla[1][10] = (ImageView) findViewById(R.id.celda1_10);
        casilla[1][10].setOnClickListener(this);

        casilla[2][10] = (ImageView) findViewById(R.id.celda2_10);
        casilla[2][10].setOnClickListener(this);

        casilla[3][10] = (ImageView) findViewById(R.id.celda3_10);
        casilla[3][10].setOnClickListener(this);

        casilla[4][10] = (ImageView) findViewById(R.id.celda4_10);
        casilla[4][10].setOnClickListener(this);

        casilla[5][10] = (ImageView) findViewById(R.id.celda5_10);
        casilla[5][10].setOnClickListener(this);


        //Inicializando valores a la matriz que representa al tablero
        for(int i=0;i<6;i++){
            for(int j=0;j<11;j++){
                T[i][j]=0;
                if(j==0){
                    //Fichas Negras     -1
                    if(i==0|i==2|i==4)
                        T[i][j]=-1;

                    //Fichas Blancas    1
                    if(i==1|i==3|i==5)
                        T[i][j]=1;
                }
            }
        }

        //Inicializamos la cantidad de fichas
        Fb=3;
        Fn=3;

        //Inicializa el Turno
        turno=0;


        if(turnoHumano == 0){
            FichasComputadora[0] = new int[]{0,0};
            FichasComputadora[1] = new int[]{2,0};
            FichasComputadora[2] = new int[]{4,0};
        }else{
            FichasComputadora[0] = new int[]{1,0};
            FichasComputadora[1] = new int[]{3,0};
            FichasComputadora[2] = new int[]{5,0};
        }

        System.out.println("Fichas de la computadora");
        synchronized(this){
            for(Object f : FichasComputadora){
                int[] fich = (int[])f;
                System.out.println(fich[0]+" "+fich[1]);
            }
            HacerJugada();
        }

    }

    //  JUGADA MAQUINA *********************************************************
    public void JugadaMaquinaPrincipiante(){
        int a,b;//POSICION INICIO
        int x,y;//POSICION FINAL
        //OBTENER LAS POSIBLES JUGADAS(ESTADOS) DE LA MAQUINA

        for(Object o : FichasComputadora){//PARA CADA FICHA DE LA COMPUTADORA
            int[] ficha = (int[])o;
            //POSICION DE LA FICHA (a,b) EN EL TABLERO
            a=ficha[0];
            b=ficha[1];

            //SI LA JUGADA QUE VA HACER ES VALIDA LA GUARDO
            if(ValidarMovimiento(a, b, a+2, b+1)){
                if(T[a+2][b+1]==0){
                    PosibleJugada.add(new int[]{a,b,a+2,b+1});  }
            }
            if(ValidarMovimiento(a, b, a+1, b+2)){
                if(T[a+1][b+2]==0){
                    PosibleJugada.add(new int[]{a,b,a+1,b+2});  }
            }
            if(ValidarMovimiento(a, b, a-1, b+2)){
                if(T[a-1][b+2]==0){
                    PosibleJugada.add(new int[]{a,b,a-1,b+2});  }
            }
            if(ValidarMovimiento(a, b, a-2, b+1)){
                if(T[a-2][b+1]==0){
                    PosibleJugada.add(new int[]{a,b,a-2,b+1});  }
            }

        }

        //DECISION ALEATORIA DE LA JUGADA
        Random generarRandom = new Random();
        int eleccion = generarRandom.nextInt(PosibleJugada.size());//ELIJE UN RANDON ENTRE 0 - TAMAÑO DEL ARRAY DE POSIBLES JUGADAS
        int[] R = (int[])PosibleJugada.get(eleccion);//GUARDO LAS COORDENADAS DE LA JUGADA QUE REALIZARA LA MAQUINA

        //COORDENADA DE PARTIDA
        a=R[0];
        b=R[1];
        //COORDENADA DE FINAL
        x=R[2];
        y=R[3];
        //QUE FICHA SE MOVIO?
        int fichaMoved=-1;
        for(int i=0;i<3;i++){
            int k[] = (int[])FichasComputadora[i];
            if(k[0]==a && k[1]==b){
                fichaMoved=i;
            }
        }
        System.out.println("---> "+fichaMoved);




        if(turnoHumano==0){
            System.out.println("***** Movimiento Maquina ******");
            MoverFichaNegra(a ,b ,x ,y ,1);
            System.out.println("De "+a+","+b+" -> "+x+","+y);
            ActualizarFicha(fichaMoved, x, y);
            PosibleJugada.clear();

        }
        if(turnoHumano==1){
            System.out.println("***** Movimiento Maquina ******");
            MoverFichaBlanca(a ,b ,x ,y ,0);
            ActualizarFicha(fichaMoved, x, y);
            PosibleJugada.clear();
        }
    }

    //USANDO EL METODO PRIMERO EL MEJOR
    public void JugadaMaquinaIntermedio(){
        int a,b;//POSICION INICIO
        int x,y;//POSICION FINAL
        //OBTENER LAS POSIBLES JUGADAS(ESTADOS) DE LA MAQUINA

        for(Object o : FichasComputadora){//PARA CADA FICHA DE LA COMPUTADORA
            int[] ficha = (int[])o;
            //POSICION DE LA FICHA (a,b) EN EL TABLERO
            a=ficha[0];
            b=ficha[1];

            //SI LA JUGADA QUE VA HACER ES VALIDA LA GUARDO
            if(ValidarMovimiento(a, b, a+2, b+1)){
                if(T[a+2][b+1]==0){
                    PosibleJugada.add(new int[]{a,b,a+2,b+1});                }
            }
            if(ValidarMovimiento(a, b, a+1, b+2)){
                if(T[a+1][b+2]==0){
                    PosibleJugada.add(new int[]{a,b,a+1,b+2});                }
            }
            if(ValidarMovimiento(a, b, a-1, b+2)){
                if(T[a-1][b+2]==0){
                    PosibleJugada.add(new int[]{a,b,a-1,b+2});                }
            }
            if(ValidarMovimiento(a, b, a-2, b+1)){
                if(T[a-2][b+1]==0){
                    PosibleJugada.add(new int[]{a,b,a-2,b+1});                }
            }
            //AHORA PosibleJugada CONTIENE TODAS LAS JUGADAS POSIBLES DE CADA FICHA QUE POSEE LA COMPUTADORA
        }

        int[] JugadaElegida = null;
        int utilidad=0,sgteUtilidad;

        //USAMOS EL METODO PRIMERO EL MEJOR
        for(int k=0;k<PosibleJugada.size();k++){//RECORRO LA LISTA DE POSIBLES JUGADAS
            if(k==0){
                System.out.println("Eligiendo MejorJugada");
                utilidad =   FuncionEvaluadora((int[])PosibleJugada.get(k));//UTILIDAD DE LA PRIMERA POSIBLE JUGADA
                System.out.println("k="+k+" Utilidad:"+utilidad);
                JugadaElegida = (int[])PosibleJugada.get(k);
                continue;
            }
            sgteUtilidad = FuncionEvaluadora((int[])PosibleJugada.get(k));

            //SI TIENE MAS UTILIDAD QUE EL ANTERIOR, REEMPLAZAMOS LA JugadaElegida
            if(utilidad<sgteUtilidad){
                JugadaElegida = (int[])PosibleJugada.get(k);
                System.out.println("k="+k+" Utilidad:"+sgteUtilidad);
                utilidad=sgteUtilidad;
            }
        }

        //COORDENADA DE PARTIDA
        a=JugadaElegida[0];
        b=JugadaElegida[1];

        //COORDENADA DE FINAL
        x=JugadaElegida[2];
        y=JugadaElegida[3];

        int fichaMoved=100;
        //QUE FICHA FUE LA QUE SE ELIGIO?
        for(int i=0;i<3;i++){
            int r = ((int[])FichasComputadora[i])[0];
            int s = ((int[])FichasComputadora[i])[1];
            if(a==r && b==s){
                fichaMoved=i;
                break;
            }
        }

        if(turnoHumano==0){
            System.out.println("***** Movimiento Maquina Intermedio******");
            System.out.println("De "+a+","+b+" -> "+x+","+y);
            MoverFichaNegra(a ,b ,x ,y ,1);
            ActualizarFicha(fichaMoved, x, y);
            PosibleJugada.clear();

        }
        if(turnoHumano==1){
            System.out.println("***** Movimiento Maquina Intermedio******");
            System.out.println("De "+a+","+b+" -> "+x+","+y);
            MoverFichaBlanca(a ,b ,x ,y ,0);
            ActualizarFicha(fichaMoved, x, y);
            PosibleJugada.clear();
        }


    }

    //USAREMOS UN ARBOL DE 2 NIVELES, USANDO EL METODO MAX-MIN

    public Integer[][] copiaEspacioEstado(Integer[][] Tablero){
        Integer[][] copiaEstado = new Integer[6][11];
        for(int i=0;i<6;i++){
            for(int j=0;j<11;j++){
                copiaEstado[i][j] = Tablero[i][j];
            }
        }
        return copiaEstado;
    }

    public void MostrarJugadaMap(Map<Object,Object> m){
        for(Object max : m.keySet()){
            int[] first = (int[])max;
            int[] second = (int[])m.get(max);
            System.out.println("Jugadas Menor Utilidad para Humano: ("+first[0]+","+first[1]+")-("+first[2]+","+first[3]+") Utilidad="+first[4]+" --> ("+second[0]+","+second[1]+")-("+second[2]+","+second[3]+") Utilidad="+second[4]);
        }
    }

    public void VisualizarArbol(Map<Object,List<Object>> arbol){

        for(Object padres : arbol.keySet()){
            int[] pad = (int[]) padres;
            System.out.println("Padre: ("+pad[0]+","+pad[1]+")->"+"("+pad[2]+","+pad[3]+")"+" Utilidad="+pad[4]);
            for(Object hijos : arbol.get(padres)){
                int[] hijo = (int[])hijos;
                System.out.println("Hijo: ("+hijo[0]+","+hijo[1]+")->"+"("+hijo[2]+","+hijo[3]+")"+" Utilidad="+hijo[4]);
            }
            System.out.println();
        }

    }

    public int ubicarFicha(int[] k){
        int fichaMoved=100;
        for(int i=0;i<3;i++){
            int r = ((int[])FichasComputadora[i])[0];
            int s = ((int[])FichasComputadora[i])[1];
            if(k[0]==r && k[1]==s){
                fichaMoved=i;
                break;
            }
        }
        return fichaMoved;
    }

    //OBTENGO LA POSICION DE LAS FICHAS DEL JUGADOR DE ESE TURNO
    public List PosicionFichas(Integer[][] Tablero,int turno){
        //Quiero las fichas negras o blancas?
        int colorFicha = (turno==0 ? 1 : -1);//0 = BLANCAS(1) | 1 = NEGRAS(-1)
        List fichasEnTablero = new ArrayList();
        for(int i=0;i<6;i++){
            for(int j=0;j<11;j++){
                if(Tablero[i][j]==colorFicha){
                    fichasEnTablero.add(new int[]{i,j});
                }
            }
        }
        return fichasEnTablero;
    }

    public int FuncionEvaluadora(int[] ficha){
        int a = ficha[0];
        int b = ficha[1];
        int x = ficha[2];
        int y = ficha[3];
        if(y==10){
            return 5;
        }else{
            return y-b;
        }
    }

    public void ActualizarFicha(int nficha,int x,int y){
        FichasComputadora[nficha]= new int[]{x,y};
    }

    //METODO QUE REALIZA LA JUGADA
    public void HacerJugada(){
        //TURNOS : BLANCAS 0 | NEGRAS 1
        if(turno==turnoHumano){/* LA PERSONA HACE SU JUGADA, EL DECIDE  */
        }
        if(turno!=turnoHumano){//TURNO DE LA MAQUINA
            switch(dificultadIA){
                case "Principiante" :
                    // try {
                    //   Thread.sleep(1000);
                    JugadaMaquinaPrincipiante();
                    //} catch (Exception e) {}
                    break;
                case "Intermedio":
                    JugadaMaquinaIntermedio();
                    break;

                case "Avanzado":
                    JugadaMaquinaIntermedio();
                    break;
            }

        }

    }

    private void TerminoJuego(){
        if(Fn==10&&Fb==10){
            alertaTerminoJuagada("HA SIDO UN EMPATE !!");
            //JOptionPane.showMessageDialog(null, "          HA SIDO UN EMPATE !!");
            //System.exit(0);
        }
        if(Fb==0){
            alertaTerminoJuagada("FELICIDADES !!!GANO LAS FICHAS BLANCAS");
            //JOptionPane.showMessageDialog(null, "FELICIDADES !!!\nGANO LAS FICHAS BLANCAS");
            //System.exit(0);
        }
        if(Fn==0){
            alertaTerminoJuagada("FELICIDADES !!!GANO LAS FICHAS NEGRAS");
            //JOptionPane.showMessageDialog(null, "FELICIDADES !!!\nGANO LAS FICHAS NEGRAS");
            //System.exit(0);
        }
    }

    private void MoverFichaBlanca(int a,int b,int x,int y,int t){
        //Valido si es su turno
        if(t==0){
            //Valido si la ficha es blanca
            if(T[a][b] == 1){
                //Valido si el destino esta vacio
                if(T[x][y] == 0){
                    //Valido si es correcto el movimiento
                    if(ValidarMovimiento(a,b,x,y)==true){
                        //Si cumple con todas las condiciones, cambiamos de estado

                        //Cambio de Turno
                        turno=1;

                        //Dejamos vacio la posicion inicial
                        T[a][b]=0;
                        casilla[a][b].setImageResource(0);

                        //Actualizamos la posicion final
                        if(y<10){//Si no ha llegado a la meta
                            T[x][y] = 1;
                            casilla[x][y].setImageResource(R.drawable.caballo_white);
                            ;
                        }else{//Si llego a la meta
                            T[x][10]=0;
                            casilla[x][10].setImageResource(0);
                        }

                        //Se actualiza el numero de fichas si llega a la meta
                        if(y>9){ Fb = Fb-1;
                            contFichaBlanca.setText(String.valueOf(Fb));
                        }

                        //FICHAS BLANCAS ************************************************************************************************************************

                        if(Fn==5){          //YA ACABO FICHAS NEGRAS
                            if(Fb==0){      //AMBOS ACABARON
                                Fn=10; Fb=10;//EMPATE
                            }else{
                                Fn=0;       //GANO NEGRAS
                            }
                        }

                        if(Fb==0){
                            Fb=5;//ACABO SUS FICHAS PRIMERO LOS BLANCOS
                        }

                        //************************************************************************************************************************
                        /*HAY UN NUEVO ESTADO
                            T[][] CAMBIO
                            Fb  CAMBIO
                            Fn  CAMBIO
                        */
                        System.out.println("Movimiento Blanco Valido\n");
                        TerminoJuego();
                        HacerJugada();
                    }
                }
            }

        }

        return;
    }

    //METODO PARA EL HUMANO QUE ESTA JUGANDO
    private void MoverFichaNegra(int a,int b,int x,int y,int t){
        //Valido si es su turno
        if(t==1){
            //Valido si la ficha es negra
            if(T[a][b] == -1){
                //Valido si el destino esta vacio
                if(T[x][y] == 0){
                    //Valido si es correcto el movimiento
                    if(ValidarMovimiento(a,b,x,y)==true){
                        //Si cumple con todas las condiciones, cambiamos de estado
                        //Cambio de Turno
                        turno=0;

                        //Dejamos vacio la posicion inicial
                        T[a][b]=0;
                        casilla[a][b].setImageResource(0);

                        //Actualizamos la posicion final
                        if(y<10){//Si no ha llegado a la meta
                            T[x][y]=-1;
                            casilla[x][y].setImageResource(R.drawable.caballo_black);
                        }else{//Si llego a la meta
                            T[x][10]=0;
                            casilla[x][10].setImageResource(0);
                        }

                        //Se actualiza el numero de fichas si llega a la meta
                        if(y>9) {
                            Fn = Fn-1;
                            contFichaNegra.setText(String.valueOf(Fn));
                        }

                        //FICHAS NEGRAS ************************************************************************************************************************
                        if(Fb==5){//LAS BLANCAS YA TERMINO
                            if(Fn==0){//TERMINO LAS FICHAS NEGRAS
                                Fb=10;  Fn=10;//EMPATA
                            }else{//NO TERMINO LAS FICHAS NEGRAS
                                Fb=0;//GANA FICHAS BLANCAS
                            }
                        }

                        if(Fn==0){
                            Fn=5;//ACABO PRIMERO SUS FICHAS LAS NEGRAS
                        }

                        //************************************************************************************************************************
                        System.out.println("Movimiento Negro Valido\n");
                        TerminoJuego();
                        HacerJugada();
                    }
                }
            }
        }
        return;
    }

    private boolean ValidarMovimiento(int a,int b,int i,int j){
        if( i==a+1 && j==b+2 && (0<=a && a<6) && (0<=i && i<6) && (0<=b && b<10) && (0<=j && j<11)){
            return true;
        }
        if( i==a-1 && j==b+2 && (0<=a && a<6) && (0<=i && i<6) && (0<=b && b<10) && (0<=j && j<11)){
            return true;
        }
        if( i==a+2 && j==b+1 && (0<=a && a<6) && (0<=i && i<6) && (0<=b && b<10) && (0<=j && j<11)){
            return true;
        }
        if( i==a-2 && j==b+1 && (0<=a && a<6) && (0<=i && i<6) && (0<=b && b<10) && (0<=j && j<11)){
            return true;
        }
        return false;
    }


    public void onClick (View v)
    {
        celdaActual = (ImageView) findViewById(v.getId());

        if(turno == turnoHumano){
            //celdaActual.setImageResource(R.drawable.hape);
            //System.out.println("Turno "+ (turno==0?"Caballo Blanco":"Caballo Negro"));
            if(PrimerClick == true){
                // BotonPresionado.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                for(int i=0;i<6;i++){
                    for(int j=0;j<11;j++){
                        //Buscamos el boton que se apreto PRIMERO
                        if(celdaActual == casilla[i][j]){

                            // if(turnoHumano == 0 && T[i][j]== 1){
                            //   celdaActual.setImageResource(R.drawable.hape);
                            // }

                            //Ficha Negra
                            if(T[i][j]==-1){
                                inicioX = i;
                                inicioY = j;



                                PrimerClick = false;
                                //System.out.println(i+" "+j+" Caballo Negro");
                                return;
                            }
                            //Ficha Blanca
                            if(T[i][j]==1){
                                inicioX = i;
                                inicioY = j;

                                PrimerClick = false;
                                //System.out.println(i+" "+j+" Caballo Blanco");
                                return;
                            }
                        }
                    }
                }}

            else{
                //MANEJAMOS CUANDO SE DA EL 2do CLICK
                //casilla[inicioX][inicioY].setBorder(BorderFactory.createLineBorder(null));

                for(int z=0;z<6;z++){
                    for(int w=0;w<11;w++){
                        //Buscamos el boton que se apreto SEGUNDO
                        if(celdaActual == casilla[z][w]){
                            int modInicioX = inicioX%2;
                            int modInicioY = inicioY%2;
                            //&& ( (modInicioX == 0 && modInicioY==0) || (modInicioX == 1 && modInicioY==1) )
                            //if(turnoHumano == 0  ){
                            //  casilla[inicioX][inicioY].setImageResource(0);
                            //}
                            //casilla[inicioX][inicioY].setImageResource(0);
                            destinoX=z;
                            destinoY=w;
                            //System.out.println(z+" "+w+" Destino");
                            //Si es ficha negra llamamos al metodo MoverFichaNegra
                             /*if(T[inicioX][inicioY]==-1){
                                 System.out.println("Moviendo ficha Negra");*/
                            MoverFichaNegra(inicioX,inicioY,destinoX,destinoY,turno);
                            //}
                            //Si es ficha blanca llamamos al metodo MoverFichaBlanca
                             /*if(T[inicioX][inicioY]==1){
                                 System.out.println("Moviendo ficha Blanca");*/
                            MoverFichaBlanca(inicioX,inicioY,destinoX,destinoY,turno);
                            //}
                            return;
                        }
                        //Habilitamos el primer click denuevo
                        PrimerClick = true;
                    }
                }
            }

            PrimerClick = true;
            for(int z=0;z<6;z++){
                for(int w=0;w<11;w++){
                    //Buscamos el boton que se apreto SEGUNDO
                    if(celdaActual == casilla[z][w]){
                        //casilla[inicioX][inicioY].setBorder(BorderFactory.createLineBorder(null));
                        //BotonPresionado.setBorder(BorderFactory.createLineBorder(null));
                        System.out.println(z+" "+w+" No es Caballo");
                    }
                }

            }
        }

    }

}

