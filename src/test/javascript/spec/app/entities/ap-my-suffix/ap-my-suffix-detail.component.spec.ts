/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SampleBrokerTestModule } from '../../../test.module';
import { APMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix-detail.component';
import { APMySuffixService } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix.service';
import { APMySuffix } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix.model';

describe('Component Tests', () => {

    describe('APMySuffix Management Detail Component', () => {
        let comp: APMySuffixDetailComponent;
        let fixture: ComponentFixture<APMySuffixDetailComponent>;
        let service: APMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [APMySuffixDetailComponent],
                providers: [
                    APMySuffixService
                ]
            })
            .overrideTemplate(APMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(APMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(APMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new APMySuffix(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.aP).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
