/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { SampleBrokerTestModule } from '../../../test.module';
import { WlanGroupMySuffixComponent } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix.component';
import { WlanGroupMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix.service';
import { WlanGroupMySuffix } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix.model';

describe('Component Tests', () => {

    describe('WlanGroupMySuffix Management Component', () => {
        let comp: WlanGroupMySuffixComponent;
        let fixture: ComponentFixture<WlanGroupMySuffixComponent>;
        let service: WlanGroupMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [WlanGroupMySuffixComponent],
                providers: [
                    WlanGroupMySuffixService
                ]
            })
            .overrideTemplate(WlanGroupMySuffixComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WlanGroupMySuffixComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WlanGroupMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new WlanGroupMySuffix(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.wlanGroups[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
