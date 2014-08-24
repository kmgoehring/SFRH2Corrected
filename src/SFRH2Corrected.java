	import java.text.DecimalFormat;
	import javax.swing.JOptionPane;

	/**
 * 
 * @author Kevin M. Goehring. 
 * 		   A brief class/UI that runs calculations off user
 *         input and returns various useful values for astronomical research
 *         concerning star formation rates and molecular hydrogen masses.
 *         Applies distance conversions, deprojected area and axis caluclations,
 *         and gas and star formation rate densities. These star formation rates are 
 *         corrected using the total infrared (TIR) luminosities from the paper
 *         "Engelbracht, C.W., Rieke, G.H., Gordon, K.D., etal. 2008, ApJ, 678, 804"
 *         Any variable naming using Englebracht refers to this paper. Hydrogen-alpha 
 *         luminosities are supplied in "Gil de Paz, A., Madore, B.F., Pevunova1, O. 2003,ApJS, 147, 29"
 */
	public class SFRH2Corrected {

		public static void main(String[] args) {
			
			// String variables for user input
			String  dAuthor, // Object distance from associated reference paper, in megaparsecs
					dYou, // User object distance, in megaparsecs
					dEngle, // Objects distance from Englebracht paper, in megaparsecs
					NedScale, // kiloparsec per arcsec scale from the NASA/IPAC extragalactic database 
					LAuthor, // Hydrogen-alpha luminosity from the Gil de Paz data, in ergs per second
					mH1, // Neutral hydrogen gas mass, in solar masses
					RC3Diameter, // RC3 de Vaucouleur's diameter, in arcseconds
					axisRatio, // Ratio between a galaxy's semi-major and semi-minor axes
					LTIR; // Total infrared luminosity, in ergs per second
			
			/**
			 * Prompt user to input data and parse the results into doubles. 
			 */
					dYou = 
							JOptionPane.showInputDialog ( "Enter the distance to your object in megaparsecs" );
					double distanceYou = Double.parseDouble(dYou);
			
					dAuthor = 
							JOptionPane.showInputDialog ( "Enter the Gil de Paz distance to your object in megaparsecs" );
					double distanceAuthor = Double.parseDouble(dAuthor);
					
					dEngle = 
							JOptionPane.showInputDialog ( "Enter the Englebracht distance to your object in megaparsecs" );
					double distanceEngle = Double.parseDouble(dEngle);
			
					NedScale = 
							JOptionPane.showInputDialog ( "Enter the Ned kpc/arcsec scale for your object." );
					double ScaleNed = Double.parseDouble(NedScale);
					
					LAuthor = 
							JOptionPane.showInputDialog ( "Enter the Gil de Paz H-alpha luminosity for your object" );
					double luminosityAuthor = Double.parseDouble(LAuthor);
					
					LTIR = 
							JOptionPane.showInputDialog ( "Enter the Englebracht TIR luminosity for your object" );
					double lTIR = Double.parseDouble(LTIR);
					
					RC3Diameter = 
							JOptionPane.showInputDialog ( "Enter the RC3 diameter for your object in arcseconds" );
					double RC3DIAMETER = Double.parseDouble(RC3Diameter);
					
					axisRatio = 
							JOptionPane.showInputDialog ( "Enter the axis ratio for your object" );
					double AxisRatio = Double.parseDouble(axisRatio);
					
					mH1 = 
							JOptionPane.showInputDialog ( "Enter the H1 mass for your object" );
					double H1MASS = Double.parseDouble(mH1);	
				
				/**
				 * The mathematical calculations. 	
				 */
				double 
				// Converted galactic diameter, in arcseconds
				NedDiameter = (RC3DIAMETER * ScaleNed * 206265) / (distanceYou * 1000),
				// Distance corrected total infrared luminosity, in ergs per second
				LTIRYOU = 3.9E33 * lTIR * (distanceYou / distanceEngle)*(distanceYou / distanceEngle),
				// Distance corrected hydrogen-alpha luminosity, in ergs per second
				LYou = luminosityAuthor*(distanceYou / distanceAuthor)*(distanceYou / distanceAuthor)*2.8,
				// Semi-major and minor axes, in arcseconds
				semiMajor = ((NedDiameter*1000000*distanceYou)/206265)/2,
				semiMinor = (((NedDiameter*AxisRatio)*1000000*distanceYou)/206265)/2,
				// Galaxy inclination, in degrees as viewed from Earth
				inclination = Math.acos((NedDiameter * AxisRatio) / NedDiameter),
				// Star formation rate, in solar masses per year
				SFR = (7.9E-42 * (LYou + .0024*LTIRYOU)),
				// Area in square parsecs and square kiloparsecs
				AreaPC = Math.PI*semiMajor*semiMinor * Math.cos(inclination),
				AreaKPC = AreaPC / 1000000,
				// Star formation rate density, in solar masses per yer per square kiloparsecs
				sigmaSFR = SFR / AreaKPC,
				// Gas mass density, in solar masses per square parsec
				sigmaGAS = Math.pow((sigmaSFR / 0.00016), (1 / 1.55)),
				// Neutral hydrogen gas mass, in solar masses
				GAS = sigmaGAS * AreaPC,
				// Molecular hydrogen gas mass, in solar masses
				mH2 = GAS - H1MASS;
				
				// Decimal formatting for presenting final results. 
				DecimalFormat df = new DecimalFormat("0.00E0");
				DecimalFormat DF = new DecimalFormat("0.0000");
				
				/**
				 * Display the results. 
				 */
				JOptionPane.showMessageDialog ( 
						null, 
						"The RC3 diameter for your object is " + DF.format(NedDiameter) + "."
						+"\n"
						+"\nL(H alpha) for your object is " + df.format(LYou) + "." 
						+"\n"
						+"\nL(TIR) for your object is " + df.format(LTIRYOU) + "."
						+"\n"
						+"\nThe star formation rate is " + DF.format(SFR) + "."
						+"\n"
						+"\nThe star formation rate density is " + DF.format(sigmaSFR) + "."
						+"\n"
						+"\nThe total gas density is " + DF.format(sigmaGAS) + "."
						+"\n"
						+"\nThe total gas mass estimate is " + df.format(GAS)
						+"\n"
						+"\nThe H2 mass estimate is " + df.format(mH2) + "."
								, "Results", 
						JOptionPane.PLAIN_MESSAGE); 
		}	
}

