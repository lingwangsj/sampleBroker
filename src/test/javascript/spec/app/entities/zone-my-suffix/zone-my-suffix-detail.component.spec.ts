/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SampleBrokerTestModule } from '../../../test.module';
import { ZoneMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/zone-my-suffix/zone-my-suffix-detail.component';
import { ZoneMySuffixService } from '../../../../../../main/webapp/app/entities/zone-my-suffix/zone-my-suffix.service';
import { ZoneMySuffix } from '../../../../../../main/webapp/app/entities/zone-my-suffix/zone-my-suffix.model';

describe('Component Tests', () => {

    describe('ZoneMySuffix Management Detail Component', () => {
        let comp: ZoneMySuffixDetailComponent;
        let fixture: ComponentFixture<ZoneMySuffixDetailComponent>;
        let service: ZoneMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [ZoneMySuffixDetailComponent],
                providers: [
                    ZoneMySuffixService
                ]
            })
            .overrideTemplate(ZoneMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ZoneMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ZoneMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ZoneMySuffix(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.zone).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
