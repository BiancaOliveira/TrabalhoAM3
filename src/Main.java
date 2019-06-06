import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import weka.clusterers.DBSCAN;
import weka.clusterers.HierarchicalClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

import weka.core.SelectedTag;
import weka.core.Tag;
import weka.core.converters.CSVLoader;

/* Paramos em entropia */


public class Main {

	public static void melhoresParametrosKmeans(Instances data, int [] VC, int [] VS, int [] VSI) throws Exception{
		double aux = 0, aux2= 0, aux3 = 0;
		SimpleKMeans kmeans = new SimpleKMeans();
		ArrayList<InstanciaBase>[] clusters = new ArrayList[kmeans.getNumClusters()];
		String linha = new String();


		for(int sem =1; sem <= 50; sem++) {
			for (int mI = 10; mI <= 500; mI += 10) {
				for (int k = 2; k <= 20; k++) {
					// Create the KMeans object.
					kmeans.setSeed(sem);
					kmeans.setNumClusters(k);
					kmeans.setMaxIterations(mI);
					kmeans.setPreserveInstancesOrder(true);
					kmeans.buildClusterer(data);

					clusters = new ArrayList[kmeans.getNumClusters()];
					for (int i = 0; i < clusters.length; i++) {
						clusters[i] = new ArrayList<>();
					}

					for (int i = 0; i < 1000; i++) {
						linha = String.valueOf(data.instance(i));
						String[] values = linha.split(",");
						InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]), Double.parseDouble(values[1]), kmeans.getAssignments()[i]);
						clusters[kmeans.getAssignments()[i]].add(instanciaBase);
					}

					//         System.out.println("\nSem = "+ sem + " Mi = " + mI + " k= " + k + " COESÃO: " + coesao(clusters));
					//Inicia as variaveis
					if (sem == 1 && mI == 10 && k == 2) {
//						aux = coesao(clusters);
//						VC[0] = sem;
//						VC[1] = mI;
//						VC[2]= k;
//						aux2 = separabilidade(clusters);
//						VS[0] = sem;
//						VS[1] = mI;
//						VS[2]= k;
						aux3 = silhueta(clusters);
						VSI[0] = sem;
						VSI[1] = mI;
						VSI[2]= k;
					}
//					//parametros coesão
//					if (aux > coesao(clusters)) {
//						aux = coesao(clusters);
//						VC[0] = sem;
//						VC[1] = mI;
//						VC[2]= k;
//					}
//					//parametros separabilidade
//					if (aux2 < separabilidade(clusters)) {
//						aux2 = separabilidade(clusters);
//						VS[0] = sem;
//						VS[1] = mI;
//						VS[2]= k;
//					}
					//parametros silhueta
					if(silhueta(clusters)> 0) {
						if (aux3 < silhueta(clusters)) {
							aux3 = silhueta(clusters);
							VSI[0] = sem;
							VSI[1] = mI;
							VSI[2] = k;
						}
					}else{
						if (aux3 > silhueta(clusters)) {
							aux3 = silhueta(clusters);
							VSI[0] = sem;
							VSI[1] = mI;
							VSI[2] = k;
						}
					}
				}
			}
		}
	}

	public static void parametrosEntropiaKmeans(Instances data, Instances data1, int [] VE) throws Exception{
		double aux = 0;
		SimpleKMeans kmeans = new SimpleKMeans();
		ArrayList<InstanciaBase>[] clusters = new ArrayList[kmeans.getNumClusters()];
		String linha = new String();


		for(int sem =1; sem <= 50; sem++) {
			for (int mI = 10; mI <= 500; mI += 10) {
				for (int k = 2; k <= 20; k++) {
					// Create the KMeans object.
					kmeans.setSeed(sem);
					kmeans.setNumClusters(k);
					kmeans.setMaxIterations(mI);
					kmeans.setPreserveInstancesOrder(true);
					kmeans.buildClusterer(data);

					clusters = new ArrayList[kmeans.getNumClusters()];

					for(int i=0; i<clusters.length;i++){
						clusters[i]= new ArrayList<>();
					}

					for (int i = 0; i < 1000; i++) {
						//System.out.println("TESTE: "+  data.instance(i));
						linha= String.valueOf(data1.instance(i));
						//System.out.println(data1.instance(i));
						String[] values = linha.split(",");
						InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), Integer.parseInt(values[2]));
						clusters[kmeans.getAssignments()[i]].add(instanciaBase);
						//System.out.println("teste: " +kmeans.getAssignments()[i]);
					}

//					 System.out.println("\nSem = "+ sem + " Mi = " + mI + " k= " + k + " Entropia: " + entropia(clusters));
					//Inicia as variaveis
					if (sem == 1 && mI == 10 && k == 2) {
						aux = entropia(clusters);
						VE[0] = sem;
						VE[1] = mI;
						VE[2]= k;
					}

					//parametros entropia
					if (aux > entropia(clusters)) {
						aux = entropia(clusters);
						VE[0] = sem;
						VE[1] = mI;
						VE[2]= k;
					}
				}
			}
		}
	}

	public static void melhoresParametrosDBSCAN(Instances data, Instances data1, double [] VC, double [] VS, double [] VE, double [] VSI) throws Exception{

		double aux = 0, aux2= 0, aux3 = 0, aux4 = 0;
		DBSCAN dbscan = new DBSCAN();
		ArrayList<InstanciaBase>[] clusters = new ArrayList[dbscan.numberOfClusters()];
		ArrayList<InstanciaBase>[] clusters2 = new ArrayList[dbscan.numberOfClusters()];
		String linha = new String(), linha2 = new String();

		for(double minP = 1; minP <= 50; minP++) {
			for (double raio = 0.2; raio <= 10; raio += 0.01) {

				dbscan.setEpsilon(raio);
				dbscan.setMinPoints((int) minP);
				dbscan.buildClusterer(data);
				//System.out.println("Dercrição clusters: "+ dbscan.toString());


				clusters = new ArrayList[dbscan.numberOfClusters()];
				clusters2 = new ArrayList[dbscan.numberOfClusters()];
				for(int i=0; i<clusters.length;i++){
					clusters[i]= new ArrayList<>();
					clusters2[i]= new ArrayList<>();
				}

				for (int i = 0; i < 1000; i++) {
					//System.out.println("TESTE: "+  data.instance(i));
					linha= String.valueOf(data.instance(i));
					linha2= String.valueOf(data1.instance(i));

					String[] values = linha.split(",");
					InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), dbscan.clusterInstance(data.instance(i)));
					clusters[dbscan.clusterInstance(data.instance(i))].add(instanciaBase);

					String[] values2 = linha2.split(",");
					InstanciaBase instanciaBase2 = new InstanciaBase(Double.parseDouble(values2[0]),Double.parseDouble(values2[1]), Integer.parseInt(values2[2]));
					clusters2[dbscan.clusterInstance(data.instance(i))].add(instanciaBase2);
					//System.out.println("teste: " +kmeans.getAssignments()[i]);
				}
//				System.out.println("\nminP = "+ minP + " raio = " + raio + " COESÃO: " + silhueta(clusters));
//				System.out.println("Separabilidade: " + separabilidade(clusters));

				//Inicia as variaveis
				if (minP == 1 && raio == 0.2) {
					aux = coesao(clusters);
					VC[0] = minP;
					VC[1] = raio;
					aux2 = separabilidade(clusters);
					VS[0] = minP;
					VS[1] = raio;
					aux3 = entropia(clusters2);
					VE[0] = minP;
					VE[1] = raio;
					aux4 = silhueta(clusters2);
					VSI[0] = minP;
					VSI[1] = raio;

				}

				//parametros coesão
				if (aux > coesao(clusters)) {
					aux = coesao(clusters);
					VC[0] = minP;
					VC[1] = raio;
				}
				//parametros separabilidade
				if (aux2 < separabilidade(clusters)) {
					aux2 = separabilidade(clusters);
					VS[0] = minP;
					VS[1] = raio;
				}

				//parametros entropia
				if (aux3 > entropia(clusters2)) {
					aux3 = entropia(clusters2);
					VE[0] = minP;
					VE[1] = raio;
				}
				//parametros silhueta
				if(silhueta(clusters)> 0) {
					if (aux4 < silhueta(clusters)) {
						aux4 = silhueta(clusters);
						VSI[0] = minP;
						VSI[1] = raio;
					}
				}else{
					if (aux4 > silhueta(clusters)) {
						aux4 = silhueta(clusters);
						VSI[0] = minP;
						VSI[1] = raio;
					}
				}
			}
		}
	}

	public static double coesao(ArrayList<InstanciaBase>[] clusters){

		double mediaCluster=0, media=0;

		for(int i=0; i<clusters.length;i++){
			mediaCluster=0;
			for(InstanciaBase a: clusters[i]){
				for(InstanciaBase b: clusters[i]){
					mediaCluster+=Math.pow((Math.sqrt(Math.pow((a.getX()-b.getX()),2)+Math.pow((a.getY()-b.getY()),2))),2);
				}
			}
			mediaCluster/=clusters[i].size();
			media+=mediaCluster;
		}

		media/=clusters.length;
//		System.out.println("COESÃO: "+media);
		return media;

	}

	public static double separabilidade(ArrayList<InstanciaBase>[] clusters){

		double somaCluster=0, media=0;

		for(int i=0; i<clusters.length;i++){
			somaCluster=0;
			for(int j=0; j<clusters.length;j++){
				if(i!=j){
					for(InstanciaBase a: clusters[i]){
						for(InstanciaBase b: clusters[j]){
							somaCluster+=Math.pow((Math.sqrt(Math.pow((a.getX()-b.getX()),2)+Math.pow((a.getY()-b.getY()),2))),2);
						}
					}
				}
			}
			media+=somaCluster;
			//System.out.println("Media="+ media);

		}

		media/=clusters.length;

//		DecimalFormat myFormatter = new DecimalFormat("###.#####");
//		String output = myFormatter.format(media);
//		System.out.println("SEPARABILIDADE: "+output);
		return media;

	}

	public static double entropia(ArrayList<InstanciaBase>[] clusters){
		double entropia, entropiaTotal=0;
		int[] elementos = new int[2];

		for(int i=0; i<clusters.length;i++){
			entropia=0;

			for (int j=0; j<elementos.length;j++){
				elementos[j]=0;
			}
			for(InstanciaBase a: clusters[i]){
				elementos[a.getClasse()-1]++;
			}

//            for (int j=0; j<elementos.length;j++){
//                System.out.println("Cluster "+i+" "+elementos[j]+" elementos");
//            }

			for(int j=0;j<elementos.length;j++){
				if(elementos[j] != 0) {
					entropia += ((double) elementos[j] / clusters[i].size()) * (Math.log((double) elementos[j] / clusters[i].size()) / Math.log(2));
				}
			}
//			System.out.println("Entropia cluster "+i+" "+entropia);
			entropiaTotal+=entropia;
		}

		entropiaTotal/=clusters.length;
		if(entropiaTotal != 0) {
			entropiaTotal *= -1;
		}
		return entropiaTotal;

	}

	public static double silhueta(ArrayList<InstanciaBase>[] clusters){
		double mediaA =0, mediaB = 0, menor = 0, si = 0, siTotal =0;

		for(int i=0; i<clusters.length;i++){
			for(InstanciaBase a: clusters[i]){
				mediaA = 0;
				si = 0;
				for(InstanciaBase b: clusters[i]){
					mediaA+=(Math.sqrt(Math.pow((a.getX()-b.getX()),2)+Math.pow((a.getY()-b.getY()),2)));
				}
				mediaA/= clusters[i].size();

				for(int j=0; j<clusters.length;j++){
					mediaB = 0;
					if(i != j) {
						for (InstanciaBase b : clusters[j]) {
							mediaB += Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
						}
						mediaB/= clusters[j].size();

						if(j == 0){
							menor = mediaB;
						}
						if(menor> mediaB){
							menor = mediaB;
						}
					}
				}
				if(mediaA > menor){
					si+= (menor -mediaA)/mediaA;
				}else{
					si+= (menor -mediaA)/menor;
				}
			}
			si/=clusters[i].size();

			siTotal+= si;
		}
		siTotal/= clusters.length;
		return siTotal;

	}

	public static void main(String[] args) throws Exception, IOException {

		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("C:\\Users\\bianc\\Documents\\Trab3AM\\Base\\teste.csv"));
		Instances data = loader.getDataSet();
		data.deleteAttributeAt(data.numAttributes()- 1);
		CSVLoader loader1 = new CSVLoader();
		loader1.setSource(new File("C:\\Users\\bianc\\Documents\\Trab3AM\\Base\\teste.csv"));
		Instances data1 = loader1.getDataSet();

		int [] VCK = new int [3], VSK = new int [3], VEK = new int [3], VSIK = new int [3];// Parametros kmeans
		double [] VCD = new double [3], VSD = new double [3], VED = new double [3], VSID = new double [3];// Parametros DBSCAN
		String linha = new String();
		ArrayList<InstanciaBase> [] clusters;

//		//Inicio Kmeans
		System.out.println("Kmean: \n");
//		//encontrando os melhores parametros
		melhoresParametrosKmeans(data,VCK, VSK,VSIK);
		parametrosEntropiaKmeans(data, data1, VEK);

		//Avaliação por coesão
		System.out.println("\n\nMétodo de avaliacão coesão");
		SimpleKMeans kmeansCoesao = new SimpleKMeans();
		kmeansCoesao.setSeed(VCK[0]);
		kmeansCoesao.setNumClusters(VCK[2]);
		kmeansCoesao.setMaxIterations(VCK[1]);
		kmeansCoesao.setPreserveInstancesOrder(true);
		kmeansCoesao.buildClusterer(data);

		clusters = new ArrayList[kmeansCoesao.getNumClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			//System.out.println("TESTE: "+  data.instance(i));
			linha= String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), kmeansCoesao.getAssignments()[i]);
			clusters[kmeansCoesao.getAssignments()[i]].add(instanciaBase);
		}

		coesao(clusters);
		System.out.println(" melhor sem= " + VCK[0]);
		System.out.println(" melhor Mi= " + VCK[1]);
		System.out.println(" melhor k= " + VCK[2]);
		System.out.println("Coesão: " + coesao(clusters));

		//Avaliação por separação

		System.out.println("Método de avaliacão Separabilidade");
		SimpleKMeans kmeansSeparabilidade= new SimpleKMeans();
		kmeansSeparabilidade.setSeed(VSK[0]);
		kmeansSeparabilidade.setNumClusters(VSK[2]);
		kmeansSeparabilidade.setMaxIterations(VSK[1]);
		kmeansSeparabilidade.setPreserveInstancesOrder(true);
		kmeansSeparabilidade.buildClusterer(data);

		clusters = new ArrayList[kmeansSeparabilidade.getNumClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha= String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), kmeansSeparabilidade.getAssignments()[i]);
			clusters[kmeansSeparabilidade.getAssignments()[i]].add(instanciaBase);
		}

		separabilidade(clusters);
		System.out.println(" melhor sem= " + VSK[0]);
		System.out.println(" melhor Mi= " + VSK[1]);
		System.out.println(" melhor k= " + VSK[2]);

		DecimalFormat myFormatter = new DecimalFormat("###.#####");
		String output = myFormatter.format(separabilidade(clusters));
		System.out.println("Separabilidade: " + output);

		//Avaliação por Entropia
		System.out.println("\n\nMétodo de avaliacão Entropia");
		SimpleKMeans kmeansEntropia = new SimpleKMeans();
		kmeansEntropia.setSeed(VEK[0]);
		kmeansEntropia.setNumClusters(VEK[2]);
		kmeansEntropia.setMaxIterations(VEK[1]);
		kmeansEntropia.setPreserveInstancesOrder(true);
		kmeansEntropia.buildClusterer(data);

		clusters = new ArrayList[kmeansEntropia.getNumClusters()];

		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha= String.valueOf(data1.instance(i));
			//System.out.println(data1.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), Integer.parseInt(values[2]));
			clusters[kmeansEntropia.getAssignments()[i]].add(instanciaBase);//
		}

		entropia(clusters);
		System.out.println(" melhor sem= " + VEK[0]);
		System.out.println(" melhor Mi= " + VEK[1]);
		System.out.println(" melhor k= " + VEK[2]);
		System.out.println("Entropia: " + entropia(clusters));

//		Avaliação por Silhueta

		System.out.println("Método de avaliacão Silhueta");
		SimpleKMeans kmeansSilhueta= new SimpleKMeans();
		kmeansSilhueta.setSeed(VSIK[0]);
		kmeansSilhueta.setNumClusters(VSIK[2]);
		kmeansSilhueta.setMaxIterations(VSIK[1]);
		kmeansSilhueta.setPreserveInstancesOrder(true);
		kmeansSilhueta.buildClusterer(data);

		clusters = new ArrayList[kmeansSilhueta.getNumClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha= String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), kmeansSilhueta.getAssignments()[i]);
			clusters[kmeansSilhueta.getAssignments()[i]].add(instanciaBase);
		}

		System.out.println(" melhor sem= " + VSIK[0]);
		System.out.println(" melhor Mi= " + VSIK[1]);
		System.out.println(" melhor k= " + VSIK[2]);
		System.out.println("Silhueta: " + silhueta(clusters));


//----------------------------------------------------------------------------------------------------------------------

        //Inicio DBSCAN
        System.out.println("DBSCAN: \n");
        melhoresParametrosDBSCAN(data, data1, VCD, VSD, VED, VSID);

        //Avaliação por coesão
		System.out.println("Método de avaliacão Coesão");
		DBSCAN dbscanCoesao = new DBSCAN();
		dbscanCoesao.setMinPoints((int) VCD[0]);
		dbscanCoesao.setEpsilon(VCD[1]);
		dbscanCoesao.buildClusterer(data);

//		System.out.println("Dercrição clusters: "+ dbscanCoesao.toString());

		clusters = new ArrayList[dbscanCoesao.numberOfClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha= String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), dbscanCoesao.clusterInstance(data.instance(i)));
			clusters[dbscanCoesao.clusterInstance(data.instance(i))].add(instanciaBase);
		}

		//coesao(clusters);
		System.out.println(" melhor minP= " + VCD[0]);
		System.out.println(" melhor raio= " + VCD[1]);
		System.out.println("Coesão: " + coesao(clusters));

		//Avaliação por separabilidade
		System.out.println("Método de avaliacão Separabilidade");
		DBSCAN dbscanSeparabilidade = new DBSCAN();
		dbscanSeparabilidade.setMinPoints((int) VSD[0]);
		dbscanSeparabilidade.setEpsilon(VSD[1]);
		dbscanSeparabilidade.buildClusterer(data);

		clusters = new ArrayList[dbscanSeparabilidade.numberOfClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha = String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]), Double.parseDouble(values[1]), dbscanSeparabilidade.clusterInstance(data.instance(i)));
			clusters[dbscanSeparabilidade.clusterInstance(data.instance(i))].add(instanciaBase);
		}

		//separabilidade(clusters);
		System.out.println(" melhor minP= " + VSD[0]);
		System.out.println(" melhor raio= " + VSD[1]);
		System.out.println("Separabilidade: " + separabilidade(clusters));



		//Avaliação por Entropia
		System.out.println("Método de avaliacão Entropia");
		DBSCAN dbscanEntropia = new DBSCAN();
		dbscanEntropia.setMinPoints((int) VED[0]);
		dbscanEntropia.setEpsilon(VED[1]);
		dbscanEntropia.buildClusterer(data);


		clusters = new ArrayList[dbscanEntropia.numberOfClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			//System.out.println("TESTE: "+  data.instance(i));
			linha= String.valueOf(data1.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), Integer.parseInt(values[2]));
			clusters[dbscanEntropia.clusterInstance(data.instance(i))].add(instanciaBase);
		}

		//entropia(clusters);
		System.out.println(" melhor minP= " + VED[0]);
		System.out.println(" melhor raio= " + VED[1]);
		System.out.println("Entropia : " + entropia(clusters));


		//Avaliação por Silhueta
		System.out.println("Método de avaliacão Silhueta");
		DBSCAN dbscanSilhueta = new DBSCAN();
		dbscanSilhueta.setMinPoints((int) VSID[0]);
		dbscanSilhueta.setEpsilon(VSID[1]);
		dbscanSilhueta.buildClusterer(data);

		clusters = new ArrayList[dbscanSilhueta.numberOfClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha = String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]), Double.parseDouble(values[1]), dbscanSilhueta.clusterInstance(data.instance(i)));
			clusters[dbscanSilhueta.clusterInstance(data.instance(i))].add(instanciaBase);
		}

		System.out.println(" melhor minP= " + VSID[0]);
		System.out.println(" melhor raio= " + VSID[1]);
		System.out.println("Silhueta: " + silhueta(clusters));
//----------------------------------------------------------------------------------------------------------------------

		//Inicio Agnes
		System.out.println("Agnes: \n");
		//Avaliação por coesão
		System.out.println("\n\nMétodo de avaliacão coesão");
		HierarchicalClusterer agnesCoesao = new HierarchicalClusterer();
		agnesCoesao.setNumClusters(1);
//		agnesCoesao.setLinkType();
		agnesCoesao.buildClusterer(data);

		clusters = new ArrayList[agnesCoesao.getNumClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha= String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), agnesCoesao.clusterInstance(data.instance(i)));
			clusters[agnesCoesao.clusterInstance(data.instance(i))].add(instanciaBase);
		}

//		System.out.println("numero de clusters" + agnesCoesao.getNumClusters());
		System.out.println("Coesão: " + coesao(clusters));


		//Avaliação por Separabilidade
		System.out.println("\n\nMétodo de avaliacão Separabilidade");
		HierarchicalClusterer agnesSeparação = new HierarchicalClusterer();
		agnesSeparação.buildClusterer(data);

		clusters = new ArrayList[agnesSeparação.getNumClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha= String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), agnesSeparação.clusterInstance(data.instance(i)));
			clusters[agnesSeparação.clusterInstance(data.instance(i))].add(instanciaBase);
		}
		DecimalFormat myFormatter2 = new DecimalFormat("###.#####");
		String output2 = myFormatter.format(separabilidade(clusters));
		System.out.println("Separabilidade: " + output2);

		//Avaliação por Entropia
		System.out.println("\n\nMétodo de avaliacão Entropia");
		HierarchicalClusterer agnesEntropia = new HierarchicalClusterer();
		agnesEntropia.buildClusterer(data);

		clusters = new ArrayList[agnesEntropia.getNumClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha= String.valueOf(data1.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), Integer.parseInt(values[2]));
			clusters[agnesEntropia.clusterInstance(data.instance(i))].add(instanciaBase);
		}

		System.out.println("Entropia: " + entropia(clusters));

	//	Avaliação por Silhueta
		System.out.println("\n\nMétodo de avaliacão Silhueta");
		HierarchicalClusterer agnesSilhueta = new HierarchicalClusterer();
		agnesSilhueta.buildClusterer(data);

		clusters = new ArrayList[agnesSilhueta.getNumClusters()];
		for(int i=0; i<clusters.length;i++){
			clusters[i]= new ArrayList<>();
		}

		for (int i = 0; i < 1000; i++) {
			linha= String.valueOf(data.instance(i));
			String[] values = linha.split(",");
			InstanciaBase instanciaBase = new InstanciaBase(Double.parseDouble(values[0]),Double.parseDouble(values[1]), agnesSilhueta.clusterInstance(data.instance(i)));
			clusters[agnesSilhueta.clusterInstance(data.instance(i))].add(instanciaBase);
		}

		System.out.println("Silhueta: " + silhueta(clusters));
	}
}

