package proprietario;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProprietarioVipTest {

	@Test
	public void testIncrementaPuntiFedelta() {
		ProprietarioVip p1=new ProprietarioVip("PRGCTEGAG15465AF","Davide","Gamba");
		assertEquals(0,p1.getPuntiFedelta());
		p1.incrementaPuntiFedelta();
		assertEquals(1,p1.getPuntiFedelta());
	}

}
