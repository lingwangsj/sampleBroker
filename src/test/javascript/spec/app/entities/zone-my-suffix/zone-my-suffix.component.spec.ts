/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SampleBrokerTestModule } from '../../../test.module';
import { ZoneMySuffixComponent } from '../../../../../../main/webapp/app/entities/zone-my-suffix/zone-my-suffix.component';
import { ZoneMySuffixService } from '../../../../../../main/webapp/app/entities/zone-my-suffix/zone-my-suffix.service';
import { ZoneMySuffix } from '../../../../../../main/webapp/app/entities/zone-my-suffix/zone-my-suffix.model';

describe('Component Tests', () => {

    describe('ZoneMySuffix Management Component', () => {
        let comp: ZoneMySuffixComponent;
        let fixture: ComponentFixture<ZoneMySuffixComponent>;
        let service: ZoneMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [ZoneMySuffixComponent],
                providers: [
                    ZoneMySuffixService
                ]
            })
            .overrideTemplate(ZoneMySuffixComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ZoneMySuffixComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ZoneMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ZoneMySuffix(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.zones[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
