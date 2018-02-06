/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SampleBrokerTestModule } from '../../../test.module';
import { WlanMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix-detail.component';
import { WlanMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix.service';
import { WlanMySuffix } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix.model';

describe('Component Tests', () => {

    describe('WlanMySuffix Management Detail Component', () => {
        let comp: WlanMySuffixDetailComponent;
        let fixture: ComponentFixture<WlanMySuffixDetailComponent>;
        let service: WlanMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [WlanMySuffixDetailComponent],
                providers: [
                    WlanMySuffixService
                ]
            })
            .overrideTemplate(WlanMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WlanMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WlanMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new WlanMySuffix(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.wlan).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
