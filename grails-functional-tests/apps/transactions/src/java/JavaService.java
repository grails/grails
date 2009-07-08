public class JavaService {
	
	private BbbbService bbbbService;
	
	public BbbbService getBbbbService() { return this.bbbbService; }
	
	public void setBbbbService(BbbbService bbbbService) {
		this.bbbbService = bbbbService;
	}
	
	public void callMe() {
		this.bbbbService.serviceMethod();
	}
}