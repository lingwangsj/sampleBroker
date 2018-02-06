/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SampleBrokerTestModule } from '../../../test.module';
import { WlanGroupMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix-detail.component';
import { WlanGroupMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix.service';
import { WlanGroupMySuffix } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix.model';

describe('Component Tests', () => {

    describe('WlanGroupMySuffix Management Detail Component', () => {
        let comp: WlanGroupMySuffixDetailComponent;
        let fixture: ComponentFixture<WlanGroupMySuffixDetailComponent>;
        let service: WlanGroupMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [WlanGroupMySuffixDetailComponent],
                providers: [
                    WlanGroupMySuffixService
                ]
            })
            .overrideTemplate(WlanGroupMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WlanGroupMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WlanGroupMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new WlanGroupMySuffix(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.wlanGroup).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
