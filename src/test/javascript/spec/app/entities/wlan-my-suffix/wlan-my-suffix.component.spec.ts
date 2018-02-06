/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SampleBrokerTestModule } from '../../../test.module';
import { WlanMySuffixComponent } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix.component';
import { WlanMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix.service';
import { WlanMySuffix } from '../../../../../../main/webapp/app/entities/wlan-my-suffix/wlan-my-suffix.model';

describe('Component Tests', () => {

    describe('WlanMySuffix Management Component', () => {
        let comp: WlanMySuffixComponent;
        let fixture: ComponentFixture<WlanMySuffixComponent>;
        let service: WlanMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [WlanMySuffixComponent],
                providers: [
                    WlanMySuffixService
                ]
            })
            .overrideTemplate(WlanMySuffixComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WlanMySuffixComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WlanMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new WlanMySuffix(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.wlans[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
